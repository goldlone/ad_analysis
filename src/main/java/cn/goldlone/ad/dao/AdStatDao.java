package cn.goldlone.ad.dao;

import cn.goldlone.ad.bean.AdStat;

import java.util.List;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 17:24
 *
 * @author CN
 * @version 1.0
 */
public interface AdStatDao {

  void updateBatch(List<AdStat> beans);

}
