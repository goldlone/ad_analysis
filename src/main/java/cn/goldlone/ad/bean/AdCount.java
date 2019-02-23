package cn.goldlone.ad.bean;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-02-23 20:03
 *
 * @author CN
 * @version 1.0
 */
public class AdCount {

  private String date;

  private int adId;

  private int count;

  public AdCount() {
  }

  public AdCount(String date, int adId, int count) {
    this.date = date;
    this.adId = adId;
    this.count = count;
  }


  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getAdId() {
    return adId;
  }

  public void setAdId(int adId) {
    this.adId = adId;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
