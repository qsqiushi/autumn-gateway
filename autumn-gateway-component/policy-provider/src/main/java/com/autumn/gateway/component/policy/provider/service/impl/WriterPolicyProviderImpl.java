package com.autumn.gateway.component.policy.provider.service.impl;

import com.autumn.gateway.component.policy.provider.enums.RedisKeyEnums;
import com.autumn.gateway.core.pojo.writer.WriterPolicy;
import com.autumn.gateway.core.service.policy.provider.IWriterPolicyProvider;
import com.autumn.gateway.data.redis.service.RedisService;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <>
 *
 * @author qiushi
 * @since 2021/9/4 11:13
 */
@Slf4j
@Service
public class WriterPolicyProviderImpl implements IWriterPolicyProvider {

  @Resource private RedisService<String> redisService;

  @Override
  public WriterPolicy getWriterPolicy() {

    String policy = redisService.get(RedisKeyEnums.AGW_WRITER_POLICY.getKeyPattern());

    JsonObject jsonObject = new JsonObject(policy);

    return jsonObject.mapTo(WriterPolicy.class);
  }
}
