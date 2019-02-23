package cn.goldlone.ad.bean;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 17:20
 *
 * @author CN
 * @version 1.0
 */
public class AdStat {

  private String date;

  private String province;

  private String city;

  private int adId;

  private int clickCount;


  public AdStat() {
  }

  public AdStat(String date, String province, String city,
                int adId, int clickCount) {
    this.date = date;
    this.province = province;
    this.city = city;
    this.adId = adId;
    this.clickCount = clickCount;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public int getAdId() {
    return adId;
  }

  public void setAdId(int adId) {
    this.adId = adId;
  }

  public int getClickCount() {
    return clickCount;
  }

  public void setClickCount(int clickCount) {
    this.clickCount = clickCount;
  }

  @Override
  public String toString() {
    return "AdStat{" +
            "date='" + date + '\'' +
            ", province='" + province + '\'' +
            ", city='" + city + '\'' +
            ", adId=" + adId +
            ", clickCount=" + clickCount +
            '}';
  }
}
