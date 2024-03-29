package com.autumn.gateway.data.redis.service;

import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis接口
 *
 * @author qius
 * @author guoxin
 * @since 2019-03-13 18:02
 */
public interface RedisService<T> {
  /**
   * redis 设置值
   *
   * @param key key
   * @param value value
   * @return boolean 结果
   */
  boolean set(String key, T value);

  /**
   * 获取值
   *
   * @param key key
   * @return T
   */
  T get(String key);

  /**
   * 执行脚本
   *
   * @param script script
   * @param keys keys
   * @param args args
   * @return T
   */
  T execute(RedisScript<T> script, List<String> keys, Object... args);

  /**
   * 指定缓存失效时间
   *
   * @param key 键
   * @param time 时间(秒)
   * @return boolean
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
   * @return Long
   */
  Long incr(String key, long delta);

  /**
   * 递减
   *
   * @param key 键
   * @param delta 要减少几(小于0)
   * @return Long
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
   * @return Double
   */
  Double hIncr(String key, String item, double by);

  /**
   * hash递减
   *
   * @param key 键
   * @param item 项
   * @param by 要减少记(小于0)
   * @return Double
   */
  Double hDecr(String key, String item, double by);

  /**
   * 根据key获取Set中的所有值
   *
   * @param key 键
   * @return Set
   */
  Set<T> sGet(String key);

  /**
   * 根据value从一个set中查询,是否存在
   *
   * @param key 键
   * @param value 值
   * @return true 存在 false不存在
   */
  boolean sHasKey(String key, T value);

  /**
   * 将数据放入set缓存
   *
   * @param key 键
   * @param values 值 可以是多个
   * @return 成功个数
   */
  long sSet(String key, T... values);

  /**
   * 将set数据放入缓存
   *
   * @param key 键
   * @param time 时间(秒)
   * @param values 值 可以是多个
   * @return 成功个数
   */
  long sSetAndTime(String key, long time, T... values);

  /**
   * 获取set缓存的长度
   *
   * @param key 键
   * @return long
   */
  long sGetSetSize(String key);

  /**
   * 移除值为value的
   *
   * @param key 键
   * @param values 值 可以是多个
   * @return 移除的个数
   */
  long setRemove(String key, T... values);

  /**
   * 获取list缓存的内容
   *
   * @param key 键
   * @param start 开始
   * @param end 结束 0 到 -1代表所有值
   * @return List
   */
  List<T> lGet(String key, long start, long end);

  /**
   * 获取list缓存的长度
   *
   * @param key 键
   * @return long
   */
  long lGetListSize(String key);

  /**
   * 通过索引 获取list中的值
   *
   * @param key 键
   * @param index 索引 index大于等于0时， 0 表头，1 第二个元素，依次类推；index小于0时，-1，表尾，-2倒数第二个元素，依次类推
   * @return T
   */
  T lGetIndex(String key, long index);

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @return boolean
   */
  boolean lSet(String key, T value);

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @param time 时间(秒)
   * @return boolean
   */
  boolean lSet(String key, T value, long time);

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @return boolean
   */
  boolean lSet(String key, List<T> value);

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @param time 时间(秒)
   * @return boolean
   */
  boolean lSet(String key, List<T> value, long time);

  /**
   * 根据索引修改list中的某条数据
   *
   * @param key 键
   * @param index 索引
   * @param value 值
   * @return boolean
   */
  boolean lUpdateIndex(String key, long index, T value);

  /**
   * 移除N个值为value
   *
   * @param key 键
   * @param count 移除多少个
   * @param value 值
   * @return 移除的个数
   */
  long lRemove(String key, long count, T value);

  /**
   * <批量删除key>
   *
   * @param pattern 参数
   * @author qius
   * @updator qius
   * @since 2020/11/3 14:22
   */
  void deleteBatch(String pattern);

  /**
   * <批量获取key>
   *
   * @param prefix 前缀
   * @return java.util.Set<java.lang.String>
   * @author qiushi
   * @updator qiushi
   * @since 2021/12/14 18:56
   */
  Set<String> getKeys(String prefix);


  Set<String> scan(String pattern);
}
