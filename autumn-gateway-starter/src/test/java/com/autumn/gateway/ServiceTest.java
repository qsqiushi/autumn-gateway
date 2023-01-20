package com.autumn.gateway;

import com.autumn.gateway.data.redis.service.RedisService;
import io.vertx.core.json.Json;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Set;

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

    Set<String> set = redisService.scan("autumn:gateway:api:url&*");

    System.out.println(Json.encode(set));


  }
}
