package com.autumn.gateway.core.service.redis.impl;

import com.autumn.gateway.core.service.redis.RedisService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisServiceImpl<T> implements RedisService<T> {

  private RedisTemplate<String, T> redisTemplate;

  public RedisServiceImpl(RedisTemplate<String, T> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 指定缓存失效时间
   *
   * @param key 键
   * @param time 时间(秒)
   * @return boolean
   */
  @Override
  public boolean expire(String key, long time) {
    try {
      if (time > 0) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 根据key 获取过期时间
   *
   * @param key 键 不能为null
   * @return 时间(秒) 返回0代表为永久有效
   */
  @Override
  public Long getExpire(String key) {
    return redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }

  /**
   * 判断key是否存在
   *
   * @param key 键
   * @return true 存在 false不存在
   */
  @Override
  public Boolean hasKey(String key) {
    try {
      return redisTemplate.hasKey(key);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 删除缓存
   *
   * @param key 可以传一个值 或多个
   */
  @Override
  @SuppressWarnings("unchecked")
  public void del(String... key) {
    if (key != null && key.length > 0) {
      if (key.length == 1) {
        redisTemplate.delete(key[0]);
      } else {
        redisTemplate.delete(CollectionUtils.arrayToList(key));
      }
    }
  }

  // ============================String=============================

  /**
   * 普通缓存获取
   *
   * @param key 键
   * @return 值
   */
  @Override
  public T get(String key) {
    return key == null ? null : redisTemplate.opsForValue().get(key);
  }

  /**
   * 普通缓存放入
   *
   * @param key 键
   * @param value 值
   * @return true成功 false失败
   */
  @Override
  public boolean set(String key, T value) {
    try {
      redisTemplate.opsForValue().set(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 普通缓存放入并设置时间
   *
   * @param key 键
   * @param value 值
   * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
   * @return true成功 false 失败
   */
  @Override
  public boolean set(String key, T value, long time) {
    try {
      if (time > 0) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
      } else {
        set(key, value);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 递增
   *
   * @param key 键
   * @param delta 要增加几(大于0)
   * @return long
   */
  @Override
  public Long incr(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递增因子必须大于0");
    }
    return redisTemplate.opsForValue().increment(key, delta);
  }

  /**
   * 递减
   *
   * @param key 键
   * @param delta 要减少几(小于0)
   * @return long
   */
  @Override
  public Long decr(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递减因子必须大于0");
    }
    return redisTemplate.opsForValue().increment(key, -delta);
  }

  // ================================Map=================================

  /**
   * HashGet
   *
   * @param key 键 不能为null
   * @param item 项 不能为null
   * @return 值
   */
  @Override
  public T hget(String key, String item) {

    HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();

    return hashOperations.get(key, item);
  }

  /**
   * 获取hashKey对应的所有键值
   *
   * @param key 键
   * @return 对应的多个键值
   */
  @Override
  public Map<String, T> hmget(String key) {

    HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();

    return hashOperations.entries(key);
  }

  /**
   * HashSet
   *
   * @param key 键
   * @param map 对应多个键值
   * @return true 成功 false 失败
   */
  @Override
  public boolean hmset(String key, Map<String, T> map) {
    try {
      redisTemplate.opsForHash().putAll(key, map);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * HashSet 并设置时间
   *
   * @param key 键
   * @param map 对应多个键值
   * @param time 时间(秒)
   * @return true成功 false失败
   */
  @Override
  public boolean hmset(String key, Map<String, T> map, long time) {
    try {
      redisTemplate.opsForHash().putAll(key, map);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key 键
   * @param item 项
   * @param value 值
   * @return true 成功 false失败
   */
  @Override
  public boolean hset(String key, String item, T value) {
    try {
      redisTemplate.opsForHash().put(key, item, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key 键
   * @param item 项
   * @param value 值
   * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
   * @return true 成功 false失败
   */
  @Override
  public boolean hset(String key, String item, T value, long time) {
    try {
      redisTemplate.opsForHash().put(key, item, value);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 删除hash表中的值
   *
   * @param key 键 不能为null
   * @param item 项 可以使多个 不能为null
   */
  @Override
  public void hDel(String key, String... item) {
    if (item != null) {
      redisTemplate.opsForHash().delete(key, item);
    }
  }

  /**
   * 判断hash表中是否有该项的值
   *
   * @param key 键 不能为null
   * @param item 项 不能为null
   * @return true 存在 false不存在
   */
  @Override
  public Boolean hHasKey(String key, String item) {
    return redisTemplate.opsForHash().hasKey(key, item);
  }

  /**
   * hash递增 如果不存在,就会创建一个 并把新增后的值返回
   *
   * @param key 键
   * @param item 项
   * @param by 要增加几(大于0)
   * @return Double
   */
  @Override
  public Double hIncr(String key, String item, double by) {
    return redisTemplate.opsForHash().increment(key, item, by);
  }

  /**
   * hash递减
   *
   * @param key 键
   * @param item 项
   * @param by 要减少记(小于0)
   * @return Double
   */
  @Override
  public Double hDecr(String key, String item, double by) {
    return redisTemplate.opsForHash().increment(key, item, -by);
  }
}
