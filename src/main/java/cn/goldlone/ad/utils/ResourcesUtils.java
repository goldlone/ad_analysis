package cn.goldlone.ad.utils;

import cn.goldlone.ad.constant.CommonConstant;
import cn.goldlone.ad.constant.RunMode;

import java.io.IOException;
import java.util.Properties;

/**
 * Description：资源文件信息读取工具类<br/>
 * Copyright (c) ， 2018， CN <br/>
 * This program is protected by copyright laws. <br/>
 *
 * @author CN
 * @version : 1.0
 */
public class ResourcesUtils {
  private static Properties properties;

  // 部署模式
  public static RunMode runMode;


  static {
    properties = new Properties();

    try {
      properties.load(ResourcesUtils.class.getClassLoader().getResourceAsStream("conf.properties"));

      //动态设置部署模式
      runMode = RunMode.valueOf(getPropertyValueByKey(CommonConstant.SPARK_JOB_DEPLOY_MODE).toUpperCase());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 根据key获得资源文件中的value
   *
   * @param key
   * @return
   */
  public static String getPropertyValueByKey(String key) {
    return properties.getProperty(key, "local");
  }
}
