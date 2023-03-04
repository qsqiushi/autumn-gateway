package com.autumn.gateway;

import com.autumn.gateway.component.policy.provider.enums.RedisKeyEnums;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;

/**
 * @program agw
 * @description
 * @author qiushi
 * @since 2021-07-27:09:01
 */
@SpringBootTest
public class PluginPolicyInsertMainTest {

  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  public void test() {
    String uuid = UUID.randomUUID().toString().replace("-", "");

    System.out.println(uuid);

    setSysPolicy(redisTemplate);

    String productId = UUID.randomUUID().toString().replace("-", "");
    String productPluginPolicyId = UUID.randomUUID().toString().replace("-", "");

    setProductPolicy(redisTemplate, productId, productPluginPolicyId);

    String productClassifyId = UUID.randomUUID().toString().replace("-", "");
    String productClassifyPluginPolicyId = UUID.randomUUID().toString().replace("-", "");
    setProductClassifyPluginPolicyId(
        redisTemplate, productClassifyId, productClassifyPluginPolicyId);

    String appId = UUID.randomUUID().toString().replace("-", "");
    String appPluginPolicyId = UUID.randomUUID().toString().replace("-", "");
    setAppPluginPolicyId(redisTemplate, productClassifyId, productClassifyPluginPolicyId);
  }

  private static void setAppPluginPolicyId(
      RedisTemplate redisTemplate, String productClassifyId, String productClassifyPluginPolicyId) {
    String appPolicyKey =
        String.format(RedisKeyEnums.AUTUMN_APP_POLICY.getKeyPattern(), productClassifyId);

    redisTemplate.opsForHash().put(appPolicyKey, "plugin", "[]");

    setEmptyPluginPolicy(redisTemplate, productClassifyPluginPolicyId, "auth");
  }

  private static void setProductClassifyPluginPolicyId(
      RedisTemplate redisTemplate, String productClassifyId, String productClassifyPluginPolicyId) {
    String productClassifyPolicyKey =
        String.format(
            RedisKeyEnums.AUTUMN_PRODUCT_CLASSIFY_POLICY.getKeyPattern(), productClassifyId);

    redisTemplate
        .opsForHash()
        .put(
            productClassifyPolicyKey,
            "plugin",
            "[{\"pluginId\":\"auth\",\"policyId\":\""
                + productClassifyPluginPolicyId
                + "\",\"name\":\"鉴权插件\"}]");

    setEmptyPluginPolicy(redisTemplate, productClassifyPluginPolicyId, "auth");
  }

  private static void setProductPolicy(
      RedisTemplate redisTemplate, String productId, String productPluginPolicyId) {
    String productPolicyKey =
        String.format(RedisKeyEnums.AUTUMN_PRODUCT_POLICY.getKeyPattern(), productId);

    redisTemplate
        .opsForHash()
        .put(
            productPolicyKey,
            "plugin",
            "[{\"pluginId\":\"auth\",\"policyId\":\""
                + productPluginPolicyId
                + "\",\"name\":\"鉴权插件\"}]");

    setEmptyPluginPolicy(redisTemplate, productPluginPolicyId, "auth");
  }

  /**
   * <设置系统策略>
   *
   * @param redisTemplate
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/19 14:11
   */
  private static void setSysPolicy(RedisTemplate redisTemplate) {

    String sysPolicyKey = RedisKeyEnums.AUTUMN_SYS_POLICY.getKeyPattern();

    redisTemplate.opsForHash().put(sysPolicyKey, "domain", "http://dev-agw.qm.com");

    redisTemplate
        .opsForHash()
        .put(
            sysPolicyKey,
            "plugin",
            "[{\"pluginId\":\"auth\",\"policyId\":\"43f0c52808c347fe99906f86ff36a321\",\"name\":\"鉴权插件\"}]");

    setEmptyPluginPolicy(redisTemplate, "43f0c52808c347fe99906f86ff36a321", "auth");
  }

  /**
   * <设置空插件策略>
   *
   * @param redisTemplate
   * @param uuid
   * @param pluginId
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/19 14:10
   */
  public static void setEmptyPluginPolicy(
      RedisTemplate redisTemplate, String uuid, String pluginId) {

    String policyKey = String.format(RedisKeyEnums.AUTUMN_PLUGIN_POLICY.getKeyPattern(), uuid);

    redisTemplate.opsForHash().put(policyKey, "content", "{}");

    redisTemplate.opsForHash().put(policyKey, "pluginId", pluginId);
  }
}
