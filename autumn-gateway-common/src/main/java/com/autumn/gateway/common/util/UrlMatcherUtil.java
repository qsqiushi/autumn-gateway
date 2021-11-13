package com.autumn.gateway.common.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Map;

/**
 * @author qiushi
 * @program  autumn-gateway
 * @description 路径匹配Url
 * @since create 2021-08-20:14:31
 */
public class UrlMatcherUtil {
  private static final PathMatcher MATCHER = new AntPathMatcher();

  /**
   * <使用spring的方法做路径匹配 UrlMatcherTest>
   *
   * @param pattern
   * @param reqPath
   * @return : java.lang.Boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/20 13:37
   */
  public static Boolean match(String pattern, String reqPath) {
    return MATCHER.match(pattern, reqPath);
  }
  /**
   * <获得路径中的参数>
   *
   * @param pattern
   * @param reqPath
   * @return : java.util.Map<java.lang.String,java.lang.String>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/20 14:32
   */
  public static Map<String, String> extractUriTemplateVariables(String pattern, String reqPath) {
    return MATCHER.extractUriTemplateVariables(pattern, reqPath);
  }
}
