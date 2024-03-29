package com.autumn.gateway.data.redis.service.impl;

import com.autumn.gateway.common.exception.BizRunTimeException;
import com.autumn.gateway.data.redis.service.RedisService;
import java.util.*;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 调用lua 脚本
 *
 * @author qius
 * @since 2019-03-27
 */
@Slf4j
@Service
public class RedisServiceImpl<T> implements RedisService<T> {

  private RedisTemplate<String, T> redisTemplate;

  @Autowired
  public RedisServiceImpl(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public T execute(RedisScript<T> script, List<String> keys, Object... args) {
    return redisTemplate.execute(script, keys, args);
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
  public void del(String... key) {
    if (key != null && key.length > 0) {
      if (key.length == 1) {
        redisTemplate.delete(key[0]);
      } else {
        redisTemplate.delete(Arrays.asList(key));
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
      throw new BizRunTimeException("递增因子必须大于0");
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
      throw new BizRunTimeException("递减因子必须大于0");
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

  /**
   * 根据key获取Set中的所有值
   *
   * @param key 键
   * @return Set
   */
  @Override
  public Set<T> sGet(String key) {
    try {
      return redisTemplate.opsForSet().members(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 根据value从一个set中查询,是否存在
   *
   * @param key 键
   * @param value 值
   * @return true 存在 false不存在
   */
  @Override
  public boolean sHasKey(String key, T value) {
    try {
      return redisTemplate.opsForSet().isMember(key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将数据放入set缓存
   *
   * @param key 键
   * @param values 值 可以是多个
   * @return 成功个数
   */
  @Override
  public long sSet(String key, T... values) {
    try {
      return redisTemplate.opsForSet().add(key, values);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 将set数据放入缓存
   *
   * @param key 键
   * @param time 时间(秒)
   * @param values 值 可以是多个
   * @return 成功个数
   */
  @Override
  public long sSetAndTime(String key, long time, T... values) {
    try {
      Long count = redisTemplate.opsForSet().add(key, values);
      if (time > 0) {
        expire(key, time);
      }
      return count;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 获取set缓存的长度
   *
   * @param key 键
   * @return long
   */
  @Override
  public long sGetSetSize(String key) {
    try {
      return redisTemplate.opsForSet().size(key);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 移除值为value的
   *
   * @param key 键
   * @param values 值 可以是多个
   * @return 移除的个数
   */
  @Override
  public long setRemove(String key, T... values) {
    try {
      return redisTemplate.opsForSet().remove(key, values);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 获取list缓存的内容
   *
   * @param key 键
   * @param start 开始
   * @param end 结束 0 到 -1代表所有值
   * @return List
   */
  @Override
  public List<T> lGet(String key, long start, long end) {
    try {
      return redisTemplate.opsForList().range(key, start, end);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取list缓存的长度
   *
   * @param key 键
   * @return long
   */
  @Override
  public long lGetListSize(String key) {
    try {
      return redisTemplate.opsForList().size(key);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 通过索引 获取list中的值
   *
   * @param key 键
   * @param index 索引 index大于等于0时， 0 表头，1 第二个元素，依次类推；index小于0时，-1，表尾，-2倒数第二个元素，依次类推
   * @return T
   */
  @Override
  public T lGetIndex(String key, long index) {
    try {
      return redisTemplate.opsForList().index(key, index);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @return boolean
   */
  @Override
  public boolean lSet(String key, T value) {
    try {
      redisTemplate.opsForList().rightPush(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @param time 时间(秒)
   * @return boolean
   */
  @Override
  public boolean lSet(String key, T value, long time) {
    try {
      redisTemplate.opsForList().rightPush(key, value);
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
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @return boolean
   */
  @Override
  public boolean lSet(String key, List<T> value) {
    try {
      redisTemplate.opsForList().rightPushAll(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   * @param time 时间(秒)
   * @return boolean
   */
  @Override
  public boolean lSet(String key, List<T> value, long time) {
    try {
      redisTemplate.opsForList().rightPushAll(key, value);
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
   * 根据索引修改list中的某条数据
   *
   * @param key 键
   * @param index 索引
   * @param value 值
   * @return boolean
   */
  @Override
  public boolean lUpdateIndex(String key, long index, T value) {
    try {
      redisTemplate.opsForList().set(key, index, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 移除N个值为value
   *
   * @param key 键
   * @param count 移除多少个
   * @param value 值
   * @return 移除的个数
   */
  @Override
  public long lRemove(String key, long count, T value) {
    try {
      return redisTemplate.opsForList().remove(key, count, value);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 批量删除key
   *
   * @param pattern key pattern
   */
  @Override
  public void deleteBatch(String pattern) {
    Set<String> keys = redisTemplate.keys(pattern + "*");

    if (!CollectionUtils.isEmpty(keys)) {
      try {
        redisTemplate.delete(keys);
      } catch (Exception ex) {
        log.error("批量删除keys失败", ex);
      }
    }
  }

  /**
   * <批量获取key>
   *
   * @param prefix 前缀
   * @return java.util.Set<java.lang.String>
   * @author qiushi
   * @updator qiushi
   * @since 2021/12/14 18:56
   */
  @Override
  public Set<String> getKeys(String prefix) {
    return redisTemplate.keys(prefix);
  }

  /**
   * scan 实现
   *
   * @param pattern 表达式，如：abc*，找出所有以abc开始的键
   */
  @Override
  public Set<String> scan(String pattern) {
    ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(10000).build();
    return redisTemplate.execute(
        (RedisCallback<Set<String>>)
            connection -> {
              Set<String> keysTmp = new HashSet<>();
              try (Cursor<byte[]> cursor = connection.scan(scanOptions)) {

                while (cursor.hasNext()) {
                  keysTmp.add(new String(cursor.next()));
                }
              } catch (Exception e) {
                throw new BizRunTimeException(e);
              }
              return keysTmp;
            });
  }
}
