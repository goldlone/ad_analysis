package cn.goldlone.ad.dao.impl;

import cn.goldlone.ad.bean.AdBlackList;
import cn.goldlone.ad.dao.AdBlackListDao;
import cn.goldlone.ad.utils.DBCPUtil;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 15:22
 *
 * @author CN
 * @version 1.0
 */
public class AdBlackListDaoImpl implements AdBlackListDao {

  private QueryRunner queryRunner = new QueryRunner(DBCPUtil.getDataSource());

  @Override
  public void insertBatch(List<AdBlackList> beans) {

    String sql = "insert " +
            "into ad_blacklist(user_id) " +
            "values(?) " +
            "on duplicate key update user_id=user_id";

    Object[][] params = new Object[beans.size()][];

    for(int i=0; i<params.length; i++) {
      AdBlackList bean = beans.get(i);
      params[i] = new Object[] {bean.getUserId()};
    }

    try {
      queryRunner.batch(sql, params);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
