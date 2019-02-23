package cn.goldlone.ad.dao;

import cn.goldlone.ad.bean.AdUserClickCount;

import java.util.List;

/**
 * Description: 统计每天各用户对各广告的点击次数功能接口 <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 11:34
 *
 * @author CN
 * @version 1.0
 */
public interface AdUserClickCountDao {

  /**
   *
   * @param list
   */
  void updateBatch(List<AdUserClickCount> list);


}
