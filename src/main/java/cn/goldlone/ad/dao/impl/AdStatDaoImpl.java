package cn.goldlone.ad.dao.impl;

import cn.goldlone.ad.bean.AdStat;
import cn.goldlone.ad.dao.AdStatDao;
import cn.goldlone.ad.utils.DBCPUtil;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 17:25
 *
 * @author CN
 * @version 1.0
 */
public class AdStatDaoImpl implements AdStatDao {

  private QueryRunner queryRunner = new QueryRunner(DBCPUtil.getDataSource());


  @Override
  public void updateBatch(List<AdStat> beans) {

    String sql = "insert " +
            "into ad_stat(date, province, city, ad_id, click_count) " +
            "values(?, ?, ?, ?, ?)" +
            "on duplicate key update click_count = ?";

    Object[][] params = new Object[beans.size()][];

    for(int i=0; i<params.length; i++) {
      AdStat adStat = beans.get(i);
      params[i] = new Object[]{adStat.getDate(), adStat.getProvince(),
              adStat.getCity(), adStat.getAdId(), adStat.getClickCount(),
              adStat.getClickCount()};
    }

    try {
      queryRunner.batch(sql, params);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
