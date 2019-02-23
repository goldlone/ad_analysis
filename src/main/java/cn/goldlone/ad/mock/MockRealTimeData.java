package cn.goldlone.ad.mock;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;


/**
 * Description：模拟实时产生的数据<br/>
 * Copyright (c) ， 2018， CN <br/>
 * This program is protected by copyright laws. <br/>
 *
 * @author CN
 * @version : 1.0
 */
public class MockRealTimeData extends Thread {

  private static final Random random = new Random();
  private static final String[] provinces = new String[]{"河北", "辽宁", "江苏", "浙江", "山东"};
  private static final Map<String, String[]> provinceCityMap = new HashMap<String, String[]>();

  private Producer<Integer, String> producer;

  public MockRealTimeData() {
    provinceCityMap.put("河北", new String[]{"石家庄", "唐山", "秦皇岛", "邯郸", "邢台", "保定", "张家口", "承德", "沧州", "廊坊", "衡水"});
    provinceCityMap.put("辽宁", new String[]{"沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东", "锦州", "营口", "阜新", "辽阳", "盘锦", "铁岭", "朝阳", "葫芦岛"});
    provinceCityMap.put("江苏", new String[]{"南京", "无锡", "徐州", "常州", "苏州", "南通", "连云港", "淮安", "盐城", "扬州", "镇江", "泰州", "宿迁"});
    provinceCityMap.put("浙江", new String[]{"杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", "金华", "衢州", "舟山", "台州", "丽水"});
    provinceCityMap.put("山东", new String[]{"济南", "青岛", "淄博", "枣庄", "东营", "烟台", "潍坊", "威海", "济宁", "泰安", "日照", "莱芜", "临沂", "德州"});
    Properties properties = new Properties();
    try {
      properties.load(MockRealTimeData.class.getClassLoader().getResourceAsStream("kafka/producer.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    producer = new KafkaProducer<Integer, String>(properties);
  }

  @Override
  public void run() {
    //每循环一次，实时模拟某个省份某个城市下的一个用户点击某一个广告的情形
    while (true) {
      String province = provinces[random.nextInt(provinces.length)];
      String[] cities = provinceCityMap.get(province);
      String city = cities[random.nextInt(cities.length)];

      //log：消息的组成，系统当前时间 省份 城市 用户id 广告id
      String log = System.currentTimeMillis() + " " + province + " " + city + " "
              + random.nextInt(100) + " " + random.nextInt(10);
      producer.send(new ProducerRecord<Integer, String>("ad_real_time_log", log));

      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 启动Kafka Producer
   *
   * @param args
   */
  public static void main(String[] args) {
    MockRealTimeData producer = new MockRealTimeData();
    producer.start();

//    Properties properties = new Properties();
//    try {
//      properties.load(MockRealTimeData.class.getClassLoader().getResourceAsStream("kafka/producer.properties"));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
  }

}
