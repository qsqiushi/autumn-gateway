package com.autumn.gateway.common.constant;

/**
 * @author qiushi
 * @program agw
 * @description http相关常量
 * @since 2022-02-12 16:50
 */
public class HttpConstants {

  public static final String APPLICATION_JSON = "application/json";

  private HttpConstants() {}

  public static final String CONTENT_LENGTH = "Content-Length";
  public static final String TRANSFER_ENCODING = "Transfer-Encoding";
  public static final String MULTIPART_FORM_DATA = "multipart/form-data";
  public static final String APPLICATION_X_WWW_FORM_URLENCODED =
      "application/x-www-form-urlencoded";

  public static final String CONTENT_TYPE = "Content-Type";

  public static final String APPLICATION_JSON_VALUE_UTF8 = "application/json;charset=utf-8";
}
