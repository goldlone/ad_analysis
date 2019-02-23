package cn.goldlone.ad.dao;

import cn.goldlone.ad.bean.AdCount;

import java.util.List;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-02-23 20:02
 *
 * @author CN
 * @version 1.0
 */
public interface AdCountDao {

  void insertClickBatch(List<AdCount> beans);

  void insertPutBatch(List<AdCount> beans);

  void insertDealBatch(List<AdCount> beans);

}
