package cn.goldlone.ad.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Description: xxx <br/>
 * Copyright 2019 CN <br/>
 * This program is protected by copyright laws.<br/>
 * Create time: 2019-02-22 10:57
 *
 * @author CN
 * @version 1.0
 */
public class HttpUtil {

  /**
   * 使用HttpURLConnection进行get请求
   * @param urlPath
   * @param params
   * @return
   */
  public static String get(String urlPath, String params) {
    try {
      urlPath += "?" + params;

      URL url = new URL(urlPath);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Charset", "UTF-8");

      if(conn.getResponseCode() == 200){
        InputStream is = conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        int len = 0;
        byte[] buf = new byte[128];
        while((len = is.read(buf)) != -1) {
          sb.append(new String(buf, 0, len));
        }
        return sb.toString();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

}
