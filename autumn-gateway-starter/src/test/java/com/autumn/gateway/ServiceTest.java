package com.autumn.gateway;

import com.autumn.gateway.data.redis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-04-27 15:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

  @Resource private RedisService<String> redisService;

  @Test
  public void test() {
    System.out.println(redisService == null);
  }
}
