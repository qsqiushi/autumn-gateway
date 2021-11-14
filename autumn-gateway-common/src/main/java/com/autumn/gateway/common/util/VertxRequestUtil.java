package com.autumn.gateway.common.util;

import io.vertx.core.MultiMap;
import io.vertx.core.json.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 处理Vertx request 中参数的一些公用方法
 *
 * @author qius
 * @date 2021/9/2 15:37
 */
public class VertxRequestUtil {
  /**
   * 将vertx的multiMap转为普通Map，否则Json工具无法将其序列化成正确的Json
   *
   * @return java.util.Map<java.lang.String, java.lang.String> @Description:
   * @param: multiMap
   * @author JiangLei
   * @date 2021/9/1 9:11
   */
  public static Map<String, Object> vertxMultiMapToSingleValueMap(MultiMap multiMap) {
    if (null != multiMap && multiMap.isEmpty() == false) {
      Map<String, Object> map = new HashMap<>();
      Set<String> names = multiMap.names();
      names.forEach(
          key -> {
            List<String> stringList = multiMap.getAll(key);
            map.put(key, stringList);
          });
      return map;
    }
    return new HashMap<>();
  }
  /**
   * 转换成单值 String Map 对于多值的话，List<String> 转换成JSON String @Description:
   *
   * @param: multiMap
   * @return java.util.Map<java.lang.String,java.lang.Object>
   * @author JiangLei
   * @date 2021/9/3 14:33
   */
  public static Map<String, String> vertxMultiMapToSingleStringMap(MultiMap multiMap) {
    if (null != multiMap && multiMap.isEmpty() == false) {
      Map<String, String> map = new HashMap<String, String>();
      Set<String> names = multiMap.names();
      names.forEach(
          key -> {
            List<String> stringList = multiMap.getAll(key);
            if (null == stringList) {
              map.put(key, null);
              return;
            }
            if (null != stringList && stringList.size() > 1) {
              map.put(key, Json.encode(stringList));
            } else {
              map.put(key, stringList.get(0));
            }
          });
      return map;
    }
    return new HashMap<String, String>();
  }
}
