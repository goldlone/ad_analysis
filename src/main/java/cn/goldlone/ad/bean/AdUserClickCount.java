package cn.goldlone.ad.bean;

/**
 * Description: 用户广告点击次数映射类 <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 11:35
 *
 * @author CN
 * @version 1.0
 */
public class AdUserClickCount {

  private String date;

  private int user_id;

  private int ad_id;

  private int click_count;

  public AdUserClickCount() {
  }

  public AdUserClickCount(String date, int user_id, int ad_id, int click_count) {
    this.date = date;
    this.user_id = user_id;
    this.ad_id = ad_id;
    this.click_count = click_count;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public int getAd_id() {
    return ad_id;
  }

  public void setAd_id(int ad_id) {
    this.ad_id = ad_id;
  }

  public int getClick_count() {
    return click_count;
  }

  public void setClick_count(int click_count) {
    this.click_count = click_count;
  }

  @Override
  public String toString() {
    return "AdUserClickCount{" +
            "date='" + date + '\'' +
            ", user_id=" + user_id +
            ", ad_id=" + ad_id +
            ", click_count=" + click_count +
            '}';
  }
}
