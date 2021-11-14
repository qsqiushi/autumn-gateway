package com.autumn.gateway.core.configer;

/**
 * 系统配置接口
 *
 * @author qius 2021/7/9
 */
public interface IConfiger {
  /**
   * 提取字符型属性值
   *
   * @param key 属性键名称
   * @return 属性值
   */
  String getStrProperty(String key);

  /**
   * 提取范型属性值
   *
   * @param key 属性键名称
   * @param <T> 属性值范型
   * @return 属性值
   */
  <T> T getProperty(String key, Class<T> clazz);

  /**
   * 提取对象值属性值
   *
   * @param key 属性键名称
   * @return 属性值
   */
  Object getPropertyVal(String key);
}
