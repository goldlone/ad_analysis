package cn.goldlone.ad.streaming

import java.util
import java.util.Date

import cn.goldlone.ad.bean._
import cn.goldlone.ad.constant.CommonConstant
import cn.goldlone.ad.dao._
import cn.goldlone.ad.dao.impl._
import cn.goldlone.ad.utils.{DateUtils, JedisUtil, ResourcesUtils}
import kafka.serializer.StringDecoder
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Description: 广告流量实时统计模块 <br/>
  * Copyright 2019 CN <br/>
  * This program is protected by copyright laws.<br/>
  * Create time: 2019-01-07 12:10
  *
  * @author CN
  * @version 1.0
  */
object AdFlowRealTimeJob {
  
  def main(args: Array[String]): Unit = {
  
    val checkpoint = "checkpoint"
    
    def createFunc(): StreamingContext = {
  
      /***  创建实例  ***/
      
      // 1. 获得StreamingContext的实例
      val config: SparkConf = new SparkConf
      config.setAppName(AdFlowRealTimeJob.getClass.getSimpleName)
      // 若是本地集群模式，需要单独设置
      if (ResourcesUtils.runMode.toString.toLowerCase().equals("local")) {
        config.setMaster("local[*]")
      }
  
      val sc: SparkContext = new SparkContext(config)
      sc.setLogLevel("WARN")
  
      val ssc: StreamingContext = new StreamingContext(sc, Seconds(2))
  
      // 2. 从消息队列中获取消息
      val kafkaStream = KafkaUtils.createDirectStream[String, String,
          StringDecoder, StringDecoder](ssc, Map("metadata.broker.list" -> "hh:9092"),
        Set("ad_real_time_log"))
      
      // 3. 设置checkpoint
      ssc.checkpoint(checkpoint)
      
      
      /***  相应的计算  ***/
      
      // 1. 实时计算每天各用户对各广告的点击量，并存入MySQL
      val perMsgDS = calAdUserClickCount(kafkaStream)
      
      // 2. 筛选出黑名单
      filterBlackList(perMsgDS)
  
      // 3. 过滤掉黑名单
      // 使用transform操作，对每个batch RDD进行处理，都动态加载MySQL中的黑名单生成RDD，
      // 然后进行join后，过滤掉batch RDD中的黑名单用户的广告点击行为
      val whiteList: DStream[String] = getAllWhiteList(kafkaStream).cache()
  
      countAdClick(whiteList)
      
      // 4. 使用updateStateByKey操作，实时计算每天各省各城市各广告的点击量，并时候更新到MySQL
      val perDayCityDS = calPerDayProvinceCityClickCnt(whiteList)
  
      // 5. 使用transform结合Spark SQL，统计每天各省份top3热门广告：
      // 首先以每天各省各城市各广告的点击量数据作为基础，首先统计出每天各省份各广告的点击量；
      // 然后启动一个异步子线程，使用Spark SQL动态将数据RDD转换为DataFrame后，注册为临时表；
      // 最后使用Spark SQL开窗函数，统计出各省份top3热门的广告，并更新到MySQL中
      calPerDayProvinceHotADTop3(perDayCityDS)
  
      //6. 使用window操作，对最近1小时滑动窗口内的数据，计算出各广告各分钟的点击量，并更新到MySQL中
      calSlideWindow(whiteList)
      
      ssc
    }
    
    
    // Streaming HA
    val ssc = StreamingContext.getOrCreate(checkpoint, createFunc)
    
    ssc.start()
    ssc.awaitTermination()
  }
  
  
  /**
    * 1. 实时计算每天各用户对各广告的点击量
    * @param dStream
    * @return
    */
  def calAdUserClickCount(dStream: DStream[(String, String)]): DStream[(String, Int)] = {
    
    // 1.1 实时计算每天各用户对各广告的点击量
    val perDayDS: DStream[(String, Int)] = dStream.map(perMsg => {
      val msgVal = perMsg._2
    
      val arr = msgVal.split("\\s+")
      val date = DateUtils.formatDate(new Date(arr(0).toLong))
      val userId = arr(3).trim
      val adId = arr(4).trim
    
      (date+"#"+userId+"#"+adId, 1)
    }).updateStateByKey[Int]((nowBatch: Seq[Int], history: Option[Int]) => {
      val nowNum: Int = nowBatch.sum
      val historySum: Int = history.getOrElse(0)
      Some(nowNum + historySum)
    })
  
    // 1.2 将每天各用户对各广告的点击次数存入MySQL
    perDayDS.foreachRDD(rdd => {
      if(!rdd.isEmpty()) {
        rdd.foreachPartition(it => {
          val dao: AdUserClickCountDao = new AdUserClickCountDaoImpl()
          val beans: util.List[AdUserClickCount] = new util.LinkedList[AdUserClickCount]()
      
          if(it.nonEmpty) {
            it.foreach(item => {
              val arr = item._1.split("#")
              val date = arr(0).trim
              val userId = arr(1).trim.toInt
              val adId = arr(2).trim.toInt
          
              val bean = new AdUserClickCount(date, userId, adId, item._2)
              beans.add(bean)
            })
          }
      
          dao.updateBatch(beans)
        })
      }
    })
    
    perDayDS
  }
  
  
  /**
    * 2. 筛选黑名单
    * 对某个广告点击超过100次，便记入黑名单
    * @param dStream
    */
  def filterBlackList(dStream: DStream[(String, Int)]): Unit = {
    dStream.filter(_._2 >= 3).foreachRDD(rdd => {
      
      rdd.foreachPartition(itr => {
        val dao: AdBlackListDao = new AdBlackListDaoImpl()
        val beans: util.List[AdBlackList] = new util.LinkedList[AdBlackList]()
        
        itr.foreach(item => {
          val arr = item._1.split("#")
          val userId = arr(1).toInt
          
          JedisUtil.sadd(CommonConstant.REDIS_BLACK_USER_LIST, arr(1))
          
          beans.add(new AdBlackList(userId))
        })
        
        dao.insertBatch(beans)
      })
      
    })
  }
  
  
  /**
    * 3. 获取所有的白名单
    * @param dStream
    * @return
    */
  def getAllWhiteList(dStream: DStream[(String, String)]): DStream[String] = {
    dStream.transform(rdd => {
      rdd.map(item => {
        val arr = item._2.split("\\s+")
        val date = arr(0).trim
        val province = arr(1).trim
        val city = arr(2).trim
        val userId = arr(3).trim
        val adId = arr(4).trim
        
        (userId, date + "#" + province + "#" + city + "#" + userId + "#" + adId)
      }).filter(item => {
        !JedisUtil.sismember(CommonConstant.REDIS_BLACK_USER_LIST, item._1)
      }).map(_._2)
    })
  }
  
  
  /**
    * 统计各个广告的点击量
    * @param dStream
    * @return
    */
  def countAdClick(dStream: DStream[String]): Unit = {
    dStream.map(item => {
      val arr = item.split("#")
      val date = arr(0)
      val adId = arr(4)
      
      (date+"#"+adId, 1)
    }).reduceByKey(_ + _).foreachRDD(rdd => {
//      rdd.foreach(item => {
//        JedisUtil.hset(CommonConstant.REDIS_AD_CLICK_COUNT_HASH, item._1, item._2.toString)
//      })
      rdd.foreachPartition(it => {
        // 写入MySQL
        val dao: AdCountDao = new AdCountDaoImpl()
        val beans: util.List[AdCount] = new util.LinkedList[AdCount]()
    
        if(it.nonEmpty) {
          it.foreach(item => {
            val arr = item._1.split("#")
            val date = arr(0)
            val adId = arr(1).toInt
            val count = item._2
        
            beans.add(new AdCount(date, adId, count))
          })
        }
    
        dao.insertClickBatch(beans)
      })
    })
  }
  
  
  /**
    * 4. 计算每天各省各城市各广告的点击量
    * @param whiteList
    */
  def calPerDayProvinceCityClickCnt(whiteList: DStream[String]): DStream[(String, Int)] = {
    val perDayDS: DStream[(String, Int)] = whiteList.map(item => {
      val arr = item.split("#")
      val date = DateUtils.formatDate(new Date(arr(0).toLong))
      (date+"#"+arr(1)+"#"+arr(2)+"#"+arr(4), 1)
    }).updateStateByKey[Int]((nowBatch: Seq[Int], history: Option[Int]) => {
      val nowNum: Int = nowBatch.sum
      val historySum: Int = history.getOrElse(0)
      Some(nowNum + historySum)
    })
    
    perDayDS.foreachRDD(rdd => {
      if(!rdd.isEmpty()) {
        rdd.foreachPartition(it => {
          val dao: AdStatDao = new AdStatDaoImpl()
          val beans: util.List[AdStat] = new util.LinkedList[AdStat]()
        
          if(it.nonEmpty) {
            it.foreach(item => {
              val arr = item._1.split("#")
              val date = arr(0).trim
              val province = arr(1).trim
              val city = arr(2).trim
              val adId = arr(3).trim.toInt
            
              val bean = new AdStat(date, province, city, adId, item._2)
              beans.add(bean)
            })
          }
        
          dao.updateBatch(beans)
        })
      }
    })
    
    perDayDS
  }
  
  
  /**
    * 5. 统计每天各省份top3热门广告
    * @param dStream
    */
  def calPerDayProvinceHotADTop3(dStream: DStream[(String, Int)]): Unit = {
    dStream.map(item => {
      val arr = item._1.split("#")
      val date = arr(0).trim
      val province = arr(1).trim
      val adId = arr(3).trim
      
      (date+"#"+province+"#"+adId, item._2)
    }).reduceByKey(_ + _)
      .foreachRDD(rdd => {

        val spark: SQLContext = new SQLContext(rdd.sparkContext)
        
        val schema: StructType = StructType(Seq(StructField("date", StringType, nullable = false),
            StructField("province", StringType, nullable = false),
            StructField("ad_id", StringType, nullable = false),
            StructField("click_count", IntegerType, nullable = false)))
        
        val tmpRDD = rdd.map(item => {
          val arr = item._1.split("#")
          Row(arr(0), arr(1), arr(2), item._2)
        })
        
        val df = spark.createDataFrame(tmpRDD, schema)
        
        df.createOrReplaceTempView("temp_ad_province")
  
        val dao: AdProvinceTop3Dao = new AdProvinceTop3DaoImpl()
        val beans: util.List[AdProvinceTop3] = new util.LinkedList[AdProvinceTop3]()
        
        val sql =
          """
            | select *, row_number() over(
            |     partition by date, province
            |     order by click_count desc
            |   ) as level
            | from temp_ad_province
            | having level <= 3
          """.stripMargin
        
        spark.sql(sql).rdd.collect().foreach(row => {
          val date = row.getAs[String](0)
          val province = row.getAs[String](1)
          val adId = row.getAs[String](2)
          val clickCount = row.getAs[Int](3)
          
          beans.add(new AdProvinceTop3(date, province, adId.toInt, clickCount))
        })
        dao.insertBatch(beans)
      })
  }
  
  
  /**
    * 6. 使用window操作，对最近1小时滑动窗口内的数据，计算出各广告各分钟的点击量，并更新到MySQL中
    * @param dStream
    */
  def calSlideWindow(dStream: DStream[String]): Unit = {
    
    dStream.map(item => {
      val arr = item.split("#")
      val date = DateUtils.formatTimeMinute(new Date(arr(0).trim.toLong))
      val adId = arr(4).trim
      
      (date+"#"+adId, 1)
    }).reduceByKeyAndWindow((v1: Int, v2: Int) => v1+v2, Minutes(60), Seconds(2))
      .foreachRDD(rdd => {
        if(!rdd.isEmpty()) {
          rdd.foreachPartition(itr => {
            val dao: AdClickTrendDao = new AdClickTrendDaoImpl()
            val beans: util.List[AdClickTrend] = new util.LinkedList[AdClickTrend]()
            
            if(itr.nonEmpty) {
              itr.foreach(item => {
                val arr = item._1.split("#")
                val tempDate = arr(0).trim
                val date = tempDate.substring(0, tempDate.length - 2)
                val minute = tempDate.substring(tempDate.length - 2)
                val adId = arr(1).trim.toInt
                val clickCount = item._2.toInt
                
                val bean = new AdClickTrend(date, adId, minute, clickCount)
                println(bean)
                beans.add(bean)
              })
              dao.insertBatch(beans)
            }
          })
        }
      })
  }
}
