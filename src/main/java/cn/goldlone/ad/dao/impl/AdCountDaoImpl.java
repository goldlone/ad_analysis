package cn.goldlone.ad.dao.impl;

import cn.goldlone.ad.bean.AdCount;
import cn.goldlone.ad.dao.AdCountDao;
import cn.goldlone.ad.utils.DBCPUtil;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-02-23 20:05
 *
 * @author CN
 * @version 1.0
 */
public class AdCountDaoImpl implements AdCountDao {

  private QueryRunner queryRunner = new QueryRunner(DBCPUtil.getDataSource());

  @Override
  public void insertClickBatch(List<AdCount> beans) {

    String sql = "insert " +
            "into ad_click_count(date, ad_id, count) " +
            "values(?, ?, ?) " +
            "on duplicate key update count=?";
    Object[][] params = new Object[beans.size()][];

    for(int i=0; i<params.length; i++) {
      AdCount bean = beans.get(i);
      params[i] = new Object[]{bean.getDate(), bean.getAdId(),
              bean.getCount(), bean.getCount()};
    }

    try {
      queryRunner.batch(sql, params);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void insertPutBatch(List<AdCount> beans) {

    String sql = "insert " +
            "into ad_put_count(date, ad_id, count) " +
            "values(?, ?, ?) " +
            "on duplicate key update count=?";
    Object[][] params = new Object[beans.size()][];

    for(int i=0; i<params.length; i++) {
      AdCount bean = beans.get(i);
      params[i] = new Object[]{bean.getDate(), bean.getAdId(),
              bean.getCount(), bean.getCount()};
    }

    try {
      queryRunner.batch(sql, params);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void insertDealBatch(List<AdCount> beans) {

    String sql = "insert " +
            "into ad_deal_count(date, ad_id, count) " +
            "values(?, ?, ?) " +
            "on duplicate key update count=?";
    Object[][] params = new Object[beans.size()][];

    for(int i=0; i<params.length; i++) {
      AdCount bean = beans.get(i);
      params[i] = new Object[]{bean.getDate(), bean.getAdId(),
              bean.getCount(), bean.getCount()};
    }

    try {
      queryRunner.batch(sql, params);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
