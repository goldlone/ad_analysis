package cn.goldlone.ad.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-01-07 16:49
 *
 * @author CN
 * @version 1.0
 */
public class JedisUtil {

  private static JedisPool jedisPool = null;

  static {
    jedisPool = new JedisPool("localhost", 6379);
  }


  public static long sadd(String key, String value) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.sadd(key, value);
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      if(jedis != null) {
        jedis.close();
      }
    }

    return 0;
  }

  public static Set<String> smembers(String key) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.smembers(key);
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      if(jedis != null) {
        jedis.close();
      }
    }

    return null;
  }

  public static boolean sismember(String key, String value) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.sismember(key, value);
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      if(jedis != null) {
        jedis.close();
      }
    }

    return false;
  }


  public static long srem(String key, String value) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.srem(key, value);
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      if(jedis != null) {
        jedis.close();
      }
    }

    return 0;
  }


  public static long hset(String key, String field, String value) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.hset(key, field, value);
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      if(jedis != null) {
        jedis.close();
      }
    }

    return 0;
  }


  public static String hget(String key, String field) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.hget(key, field);
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      if(jedis != null) {
        jedis.close();
      }
    }

    return null;
  }



}
