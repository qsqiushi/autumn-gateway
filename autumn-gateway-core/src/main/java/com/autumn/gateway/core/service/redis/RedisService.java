package com.autumn.gateway.core.service.redis;

import java.util.Map;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-09-04:10:01
 */
public interface RedisService<T> {
  /**
   * @param key
   * @param value
   * @return
   */
  boolean set(String key, T value);

  /**
   * @param key
   * @return
   */
  T get(String key);

  /**
   * 指定缓存失效时间
   *
   * @param key 键
   * @param time 时间(秒)
   * @return
   */
  boolean expire(String key, long time);

  /**
   * 根据key 获取过期时间
   *
   * @param key 键 不能为null
   * @return 时间(秒) 返回0代表为永久有效
   */
  Long getExpire(String key);

  /**
   * 判断key是否存在
   *
   * @param key 键
   * @return true 存在 false不存在
   */
  Boolean hasKey(String key);

  /**
   * 删除缓存
   *
   * @param key 可以传一个值 或多个
   */
  void del(String... key);

  /**
   * 普通缓存放入并设置时间f
   *
   * @param key 键
   * @param value 值
   * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
   * @return true成功 false 失败
   */
  boolean set(String key, T value, long time);

  /**
   * 递增
   *
   * @param key 键
   * @param delta 要增加几(大于0)
   * @return
   */
  Long incr(String key, long delta);

  /**
   * 递减
   *
   * @param key 键
   * @param delta 要减少几(小于0)
   * @return
   */
  Long decr(String key, long delta);

  /**
   * HashGet
   *
   * @param key 键 不能为null
   * @param item 项 不能为null
   * @return 值
   */
  T hget(String key, String item);

  /**
   * 获取hashKey对应的所有键值
   *
   * @param key 键
   * @return 对应的多个键值
   */
  Map<String, T> hmget(String key);

  /**
   * HashSet
   *
   * @param key 键
   * @param map 对应多个键值
   * @return true 成功 false 失败
   */
  boolean hmset(String key, Map<String, T> map);

  /**
   * HashSet 并设置时间
   *
   * @param key 键
   * @param map 对应多个键值
   * @param time 时间(秒)
   * @return true成功 false失败
   */
  boolean hmset(String key, Map<String, T> map, long time);

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key 键
   * @param item 项
   * @param value 值
   * @return true 成功 false失败
   */
  boolean hset(String key, String item, T value);

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key 键
   * @param item 项
   * @param value 值
   * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
   * @return true 成功 false失败
   */
  boolean hset(String key, String item, T value, long time);

  /**
   * 删除hash表中的值
   *
   * @param key 键 不能为null
   * @param item 项 可以使多个 不能为null
   */
  void hDel(String key, String... item);

  /**
   * 判断hash表中是否有该项的值
   *
   * @param key 键 不能为null
   * @param item 项 不能为null
   * @return true 存在 false不存在
   */
  Boolean hHasKey(String key, String item);

  /**
   * hash递增 如果不存在,就会创建一个 并把新增后的值返回
   *
   * @param key 键
   * @param item 项
   * @param by 要增加几(大于0)
   * @return
   */
  Double hIncr(String key, String item, double by);

  /**
   * hash递减
   *
   * @param key 键
   * @param item 项
   * @param by 要减少记(小于0)
   * @return
   */
  Double hDecr(String key, String item, double by);
}
