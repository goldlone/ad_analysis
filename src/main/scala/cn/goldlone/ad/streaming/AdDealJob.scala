package cn.goldlone.ad.streaming

import java.util
import java.util.Date

import cn.goldlone.ad.bean.AdCount
import cn.goldlone.ad.dao.AdCountDao
import cn.goldlone.ad.dao.impl.AdCountDaoImpl
import cn.goldlone.ad.utils.{DateUtils, ResourcesUtils}
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Description: xxx <br/>
  * Copyright 2019 CN <br/>
  * This program is protected by copyright laws.<br/>
  * Create time: 2019-02-22 17:28
  *
  * @author CN
  * @version 1.0
  */
object AdDealJob {
  
  def main(args: Array[String]): Unit = {
  
    val sparkConf = new SparkConf().setAppName(AdDealJob.getClass.getSimpleName)

    // 若是本地集群模式，需要单独设置
    if (ResourcesUtils.runMode.toString.toLowerCase().equals("local")) {
      sparkConf.setMaster("local[*]")
    }

    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")

    val ssc = new StreamingContext(sc, Seconds(2))

    val kafkaStream = KafkaUtils.createDirectStream[String, String,
        StringDecoder, StringDecoder](ssc, Map("metadata.broker.list" -> "hh:9092"),
      Set("ad_deal_log"))

    ssc.checkpoint("checkpoint-ad-deal")

    countAdDeal(kafkaStream)

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
    
  }
  
  def countAdDeal(dStream: DStream[(String, String)]): Unit = {
    
    dStream.mapPartitions(it => {
      val list = new ListBuffer[(String, Int)]
      it.foreach(perMsg => {
        val msgVal = perMsg._2
        val arr = msgVal.split("\\^A")
        val date = DateUtils.formatDate(new Date((arr(1).toDouble*1000).toLong))
        val map = parseParams(arr(3))
        val adId = map.getOrElse("adId", null)
        if(adId != null) {
          list.append((date+"#"+adId, 1))
        }
      })
      
      list.iterator
    }).reduceByKey(_ + _).updateStateByKey((nowBatch: Seq[Int], history: Option[Int]) => {
      val nowNum = nowBatch.sum
      val historyNum = history.getOrElse(0)
      
      Some(nowNum + historyNum)
    }).foreachRDD(rdd => {
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
        
        dao.insertDealBatch(beans)
      })
    })
  }
  
  def parseParams(params: String): mutable.HashMap[String, String] = {
    val map = new mutable.HashMap[String, String]()
    
    val arr = params.split("&")
    for(item <- arr) {
      val ps = item.split("=")
      if(ps.length == 2) {
        map.put(ps(0), ps(1))
      }
    }
    
    map
  }
  
}
