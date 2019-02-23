package cn.goldlone.ad.dao.impl;

import cn.goldlone.ad.bean.AdClickTrend;
import cn.goldlone.ad.dao.AdClickTrendDao;
import cn.goldlone.ad.utils.DBCPUtil;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 22:51
 *
 * @author CN
 * @version 1.0
 */
public class AdClickTrendDaoImpl implements AdClickTrendDao {

  private QueryRunner queryRunner = new QueryRunner(DBCPUtil.getDataSource());

  @Override
  public void insertBatch(List<AdClickTrend> beans) {

    String sql = "insert " +
            "into ad_click_trend(date, ad_id, minute, click_count) " +
            "values(?, ?, ?, ?) " +
            "on duplicate key update click_count=?";

    Object[][] params = new Object[beans.size()][];

    for(int i=0; i<params.length; i++) {
      AdClickTrend bean = beans.get(i);
      params[i] = new Object[]{bean.getDate(), bean.getAdId(),
              bean.getMinute(), bean.getClickCount(), bean.getClickCount()};
    }

    try {
      queryRunner.batch(sql, params);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }




}
