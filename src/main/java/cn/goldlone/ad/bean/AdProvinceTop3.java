package cn.goldlone.ad.bean;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 19:36
 *
 * @author CN
 * @version 1.0
 */
public class AdProvinceTop3 {

  private String date;

  private String province;

  private int adId;

  private int clickCount;

  public AdProvinceTop3() {
  }

  public AdProvinceTop3(String date, String province,
                        int adId, int clickCount) {
    this.date = date;
    this.province = province;
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
}
