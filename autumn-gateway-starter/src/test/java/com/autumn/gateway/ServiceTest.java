package com.autumn.gateway;

import com.autumn.gateway.data.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-04-27 15:42
 */
@SpringBootTest
public class ServiceTest {

  @Resource private RedisService<String> redisService;

  @Test
  public void test() {
    System.out.println(redisService == null);

    redisService.getKeysValues();
  }
}
