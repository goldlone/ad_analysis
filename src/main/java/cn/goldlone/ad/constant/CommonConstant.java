package cn.goldlone.ad.constant;

/**
 * Description：xxx<br/>
 * Copyright (c) ， 2018， CN <br/>
 * This program is protected by copyright laws. <br/>
 * Date：2018年11月26日
 *
 * @author CN
 * @version : 1.0
 */
public interface CommonConstant {

  /**
   * 部署模式
   */
  String SPARK_JOB_DEPLOY_MODE = "spark.job.deploy.mode";

  /**
   * 数据库连接信息共通的资源文件名
   */
  String DBCP_CONFIG_FILE = "dbcp.config.file";


  /**
   * 通用配置文件
   */
  String COMMON_CONFIG_FILE_NAME = "common.config.file.name";



//  /**
//   * 黑名单判定标准：每天对某个广告点击超过100次的用户
//   */
//  String BLACK_LIST_CNT = "black.list.cnt";
  /**
   * Redis黑名单键名
   */
  String REDIS_BLACK_USER_LIST = "redis.black.user.list";

  /**
   * Redis广告点击统计
   */
  String REDIS_AD_CLICK_COUNT_HASH = "redis.ad.click.count";


}
