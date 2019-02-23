package cn.goldlone.ad.mock;

import cn.goldlone.ad.utils.HttpUtil;

import java.util.Random;

/**
 * Description: 模拟广告数据 <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-02-22 10:52
 *
 * @author CN
 * @version 1.0
 */
public class MockNginxData extends Thread {

  private final Random random = new Random();

  @Override
  public void run() {

    String[] list = null;
    String[] list2 = null;

    while(true) {
      try {
        list = new String[5];
        list2 = new String[3];

        // 生成5条推送记录
        for(int i=0; i<5; i++) {
          int userId = random.nextInt(100);
          int adId = random.nextInt(100);
          list[i] = String.valueOf(userId) + "#" + String.valueOf(adId);

          mockPutData(userId, adId);
        }

        // 生成3条点击记录
        for(int i=0; i<3; i++) {
          int index = random.nextInt(5);
          list2[i] = list[index];
          String[] arr = list[index].split("#");
          int userId = Integer.parseInt(arr[0]);
          int adId = Integer.parseInt(arr[1]);

          mockClickData(userId, adId);
        }

        // 生成1条交易记录
        int index = random.nextInt(3);
        String[] arr = list2[index].split("#");
        int userId = Integer.parseInt(arr[0]);
        int adId = Integer.parseInt(arr[1]);
        mockDealData(userId, adId);

        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * 模拟广告点击数据
   */
  private void mockClickData(int userId, int adId) {
    HttpUtil.get("http://hh:91/adClick", "userId="+userId+"&adId="+adId);
  }

  /**
   * 模拟广告推送数据
   */
  private void mockPutData(int userId, int adId) {
    HttpUtil.get("http://hh:91/adPut", "userId="+userId+"&adId="+adId);
  }

  /**
   * 模拟商品交易数据
   */
  private void mockDealData(int userId, int adId) {
    HttpUtil.get("http://hh:91/adDeal", "userId="+userId+"&adId="+adId);
  }

  public static void main(String[] args) {

    MockNginxData mockNginxData = new MockNginxData();

    mockNginxData.start();
  }

}
