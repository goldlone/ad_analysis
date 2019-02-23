package cn.goldlone.ad.bean;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 15:12
 *
 * @author CN
 * @version 1.0
 */
public class AdBlackList {

  private int userId;

  public AdBlackList() {
  }

  public AdBlackList(int userId) {
    this.userId = userId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "AdBlackList{" +
            "userId=" + userId +
            '}';
  }
}
