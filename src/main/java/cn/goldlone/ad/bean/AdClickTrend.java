package cn.goldlone.ad.bean;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 22:49
 *
 * @author CN
 * @version 1.0
 */
public class AdClickTrend {

  private String date;


  private int adId;


  private String minute;


  private int clickCount;


  public AdClickTrend() {
  }

  public AdClickTrend(String date, int adId,
                      String minute, int clickCount) {
    this.date = date;
    this.adId = adId;
    this.minute = minute;
    this.clickCount = clickCount;
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

  public String getMinute() {
    return minute;
  }

  public void setMinute(String minute) {
    this.minute = minute;
  }

  public int getClickCount() {
    return clickCount;
  }

  public void setClickCount(int clickCount) {
    this.clickCount = clickCount;
  }

  @Override
  public String toString() {
    return "AdClickTrend{" +
            "date='" + date + '\'' +
            ", adId=" + adId +
            ", minute='" + minute + '\'' +
            ", clickCount=" + clickCount +
            '}';
  }
}
