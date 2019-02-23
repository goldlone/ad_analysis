package cn.goldlone.ad.dao.impl;

import cn.goldlone.ad.bean.AdProvinceTop3;
import cn.goldlone.ad.dao.AdProvinceTop3Dao;
import cn.goldlone.ad.utils.DBCPUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 19:43
 *
 * @author CN
 * @version 1.0
 */
public class AdProvinceTop3DaoImpl implements AdProvinceTop3Dao {

  private QueryRunner queryRunner = new QueryRunner();

  @Override
  public void insertBatch(List<AdProvinceTop3> beans) {

    Connection conn = DBCPUtil.getConnection();
    try {
      conn.setAutoCommit(false);

      String deleteSql = "delete " +
              "from ad_province_top3 " +
              "where date=? and " +
              "   province=?";

      String insertSql = "insert " +
              "into ad_province_top3(date, province, ad_id, click_count) " +
              "values(?, ?, ?, ?)" +
              "on duplicate key update click_count = ?";

      int len = beans.size();
      Object[][] deleteParams = new Object[len][];
      Object[][] insertParams = new Object[len][];

      for(int i=0; i<len; i++) {
        AdProvinceTop3 bean = beans.get(i);
        deleteParams[i] = new Object[]{bean.getDate(), bean.getProvince()};
        insertParams[i] = new Object[]{bean.getDate(), bean.getProvince(), bean.getAdId(),
                bean.getClickCount(), bean.getClickCount()};
      }

      queryRunner.batch(conn, deleteSql, deleteParams);
      queryRunner.batch(conn, insertSql, insertParams);

      DbUtils.commitAndClose(conn);
    } catch (SQLException e) {
      e.printStackTrace();
      DbUtils.rollbackAndCloseQuietly(conn);
    }

  }


}
