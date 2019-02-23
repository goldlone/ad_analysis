package cn.goldlone.ad.dao.impl;

import cn.goldlone.ad.bean.AdUserClickCount;
import cn.goldlone.ad.dao.AdUserClickCountDao;
import cn.goldlone.ad.utils.DBCPUtil;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Description: 统计每天各用户对各广告的点击次数功能接口实现类 <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 11:38
 *
 * @author CN
 * @version 1.0
 */
public class AdUserClickCountDaoImpl implements AdUserClickCountDao {

  private QueryRunner queryRunner = new QueryRunner(DBCPUtil.getDataSource());


  @Override
  public void updateBatch(List<AdUserClickCount> list) {

    String sql = "insert " +
            "into ad_user_click_count(date, user_id, ad_id, click_count) " +
            "values(?, ?, ?, ?) " +
            "on duplicate key update click_count = ?";

    Object[][] params = new Object[list.size()][];

    for(int i=0; i<params.length; i++) {
      AdUserClickCount adUserClickCount = list.get(i);
      params[i] = new Object[]{adUserClickCount.getDate(), adUserClickCount.getUser_id(),
              adUserClickCount.getAd_id(), adUserClickCount.getClick_count(),
              adUserClickCount.getClick_count()};
    }

    try {
      queryRunner.batch(sql, params);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
