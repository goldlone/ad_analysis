package cn.goldlone.ad.utils;

import cn.goldlone.ad.constant.CommonConstant;
import cn.goldlone.ad.constant.RunMode;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 11:46
 *
 * @author CN
 * @version 1.0
 */
public class DBCPUtil {

  private static DataSource dataSource;

  static {
    try {
      Properties properties = new Properties();
      /**
       * 根据conf.properties中的配置信息来判断是执行本地 测试还是生产环境
       */
      RunMode runMode = ResourcesUtils.runMode;

      String filePath = runMode.toString().toLowerCase() + File.separator +
              ResourcesUtils.getPropertyValueByKey(CommonConstant.DBCP_CONFIG_FILE);

//      System.out.println("filePath = "+filePath);


      InputStream in = DBCPUtil.class.getClassLoader().getResourceAsStream(filePath);

      properties.load(in);

      //连接池的实例
      dataSource = BasicDataSourceFactory.createDataSource(properties);
    } catch (Exception e) {//初始化异常
      throw new ExceptionInInitializerError(e);
    }
  }

  /**
   * 获得数据源（连接池）
   *
   * @return
   */
  public static DataSource getDataSource() {
    return dataSource;
  }

  /**
   * 从连接池中获得连接的实例
   *
   * @return
   */
  public static Connection getConnection() {
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

}
