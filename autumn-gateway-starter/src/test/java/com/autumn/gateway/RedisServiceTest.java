package com.autumn.gateway;

import com.autumn.gateway.common.util.ApplicationContextUtil;
import com.autumn.gateway.data.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-18 16:36
 */
@SpringBootTest
public class RedisServiceTest {

  @Autowired
  private RedisService redisService;

  @Test
  public void test() {
    Long time = redisService.getExpire("autumn:app:policy:9c780e8d68034e07bb317261e5f004c3");
    System.out.println(time);
    Map map = ApplicationContextUtil.getBeansOfType(RedisTemplate.class);

    System.out.println(map.size());

    System.out.println(map.keySet());
  }
}
