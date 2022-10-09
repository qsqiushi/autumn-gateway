package com.autumn.gateway;

import com.autumn.gateway.component.api.discover.enums.RedisKeyEnums;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @program autumn
 * @description
 * @author qiushi
 * @since 2021-07-27:09:01
 */
@SpringBootTest
public class ApiInsertMainTest {

  @Resource(name="redisTemplate") private RedisTemplate redisTemplate;

  @Test
  public void insertApiTest2() {
    //
    String uuid = UUID.randomUUID().toString().replace("-", "");

    System.out.println(uuid);

    String apiKey = String.format(RedisKeyEnums.GATEWAY_API_URL.getKeyPattern(), "/api/test2");

    redisTemplate.opsForHash().put(apiKey, "apiId", "0a142d51cebf431bb0a31f6794835edf");
    redisTemplate.opsForHash().put(apiKey, "apiClassifyId", "7f6f3a0d0d5148b2abda545acb59a9b3");
    redisTemplate.opsForHash().put(apiKey, "productClassifyId", "076c7b16eae04fd0a062ac2f76b5b461");
    redisTemplate.opsForHash().put(apiKey, "productId", "0a356fdff7584902bf689ea52867b88d");
    redisTemplate.opsForHash().put(apiKey, "name", "TestApi1");
    redisTemplate.opsForHash().put(apiKey, "useMock", false);
    redisTemplate.opsForHash().put(apiKey, "mockTimeOut", 200);
    redisTemplate.opsForHash().put(apiKey, "mockData", "{\"mockReturn\":\"message\"}");

    redisTemplate.opsForHash().put(apiKey, "bidirectionalSSL", false);
    redisTemplate.opsForHash().put(apiKey, "method", "GET");
    redisTemplate.opsForHash().put(apiKey, "serviceHttpMethod", "GET");
    redisTemplate.opsForHash().put(apiKey, "protocol", "HTTP");
    redisTemplate.opsForHash().put(apiKey, "contentType", "application/json");
    redisTemplate.opsForHash().put(apiKey, "requestProtocol", "HTTP");
    redisTemplate.opsForHash().put(apiKey, "url", "/api/test2");
    redisTemplate
        .opsForHash()
        .put(apiKey, "connectUrl", "http://10.133.65.137:8082/localfiletest/AllInOne");
    redisTemplate.opsForHash().put(apiKey, "idempotent", "non");
    redisTemplate.opsForHash().put(apiKey, "bodyPassThrough", true);
    redisTemplate.opsForHash().put(apiKey, "responseResultPassThrough", true);
    redisTemplate.opsForHash().put(apiKey, "timeOut", 2000);
    redisTemplate.opsForHash().put(apiKey, "pathJoin", false);
    redisTemplate.opsForHash().put(apiKey, "pathMatch", false);
    redisTemplate.opsForHash().put(apiKey, "rpcServiceName", "");
    redisTemplate.opsForHash().put(apiKey, "rpcMethodName", "");
    redisTemplate.opsForHash().put(apiKey, "rpcServiceUrl", "");
    redisTemplate.opsForHash().put(apiKey, "healthExam", false);
    redisTemplate.opsForHash().put(apiKey, "healthStatus", true);

    redisTemplate
        .opsForHash()
        .put(
            apiKey,
            "plugin",
            "[{\"pluginId\":\"divide\",\"policyId\":\"a0fb8b75123f49d881e0ab0bd500a698\",\"name\":\"负载均衡分配插件\"},{\"pluginId\":\"httpProxy\",\"policyId\":\"a0fb8b75123f49d881e0ab0bd500a698\",\"name\":\"http代理插件\"}]");

    redisTemplate.opsForHash().put(apiKey, "routingStgy", true);

    redisTemplate
        .opsForHash()
        .put(
            apiKey,
            "routingStgyList",
            "[{\"pathJoin\":0,\"connectUrl\":\"http://10.133.65.137:8082/localfiletest/AllInOne\",\"rpcServiceName\":\"\",\"routeParamList\":[{\"gmtUpdate\":\"2021-07-20 09:23:55.000\",\"paramLocation\":\"QueryParam\",\"id\":\"3d1de54e4b954ebc9f4da67bc7a00d07\",\"paramName\":\"queryParam\",\"sort\":1,\"ytenantId\":\"75c13acc-ae53-4694-9a6e-3ddcce26d7f3\",\"gmtCreate\":\"2021-07-20 09:23:55.000\",\"apiId\":\"4b3f930f03cb488f9fd7fe4e35fdac64\",\"policyId\":\"a5ad680e121e4868ac09a97b641aae69\",\"paramCondition\":0,\"paramValue\":\"8082\"}],\"rpcMethodName\":\"\",\"sort\":1,\"ytenantId\":\"75c13acc-ae53-4694-9a6e-3ddcce26d7f3\",\"gmtCreate\":\"2021-07-20 09:23:55.000\",\"timeOut\":60,\"serviceHttpMethod\":\"POST\",\"stgyName\":\"10.133.65.137:8082\",\"effeMode\":0,\"gmtUpdate\":\"2021-07-20 09:23:55.000\",\"rpcServiceUrl\":\"\",\"id\":\"a5ad680e121e4868ac09a97b641aae69\",\"rpcAppName\":\"\",\"apiId\":\"4b3f930f03cb488f9fd7fe4e35fdac64\"},{\"pathJoin\":0,\"connectUrl\":\"http://10.133.65.137:8083/8083\",\"rpcServiceName\":\"\",\"routeParamList\":[{\"gmtUpdate\":\"2021-07-20 09:23:55.000\",\"paramLocation\":\"QueryParam\",\"id\":\"9fc12abe726049e79c368daac24d151a\",\"paramName\":\"queryParam\",\"sort\":1,\"ytenantId\":\"75c13acc-ae53-4694-9a6e-3ddcce26d7f3\",\"gmtCreate\":\"2021-07-20 09:23:55.000\",\"apiId\":\"4b3f930f03cb488f9fd7fe4e35fdac64\",\"policyId\":\"24743fdb4bda43ba8daa6774b6b7c7f0\",\"paramCondition\":0,\"paramValue\":\"8083\"}],\"rpcMethodName\":\"\",\"sort\":2,\"ytenantId\":\"75c13acc-ae53-4694-9a6e-3ddcce26d7f3\",\"gmtCreate\":\"2021-07-20 09:23:55.000\",\"timeOut\":10,\"serviceHttpMethod\":\"POST\",\"stgyName\":\"10.133.65.137:8083\",\"effeMode\":0,\"gmtUpdate\":\"2021-07-20 09:23:55.000\",\"rpcServiceUrl\":\"\",\"id\":\"24743fdb4bda43ba8daa6774b6b7c7f0\",\"rpcAppName\":\"\",\"apiId\":\"4b3f930f03cb488f9fd7fe4e35fdac64\"}]");
    redisTemplate
        .opsForHash()
        .put(
            apiKey,
            "paramList",
            "[{\"requestType\":\"HeaderParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"access-identity-value\",\"type\":\"string\",\"required\":false},{\"requestType\":\"HeaderParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"access-recognize-key\",\"type\":\"string\",\"required\":false},{\"requestType\":\"HeaderParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"access-identity-type\",\"type\":\"string\",\"required\":false},{\"requestType\":\"BodyParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"bodyParam\",\"type\":\"string\",\"required\":false},{\"requestType\":\"QueryParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"queryParam\",\"type\":\"string\",\"required\":false}]");
    redisTemplate
        .opsForHash()
        .put(
            apiKey,
            "serviceInfo",
            "{\"method\":\"POST\",\"bodyPassthrough\":true,\"paramList\":[{\"agg\":false,\"requestType\":\"HeaderParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"access-identity-value\",\"paramList\":[],\"mapName\":\"access-identity-value\",\"mapRequestParamType\":\"HeaderParam\",\"type\":\"string\"},{\"agg\":false,\"requestType\":\"HeaderParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"access-recognize-key\",\"paramList\":[],\"mapName\":\"access-recognize-key\",\"mapRequestParamType\":\"HeaderParam\",\"type\":\"string\"},{\"agg\":false,\"requestType\":\"HeaderParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"access-identity-type\",\"paramList\":[],\"mapName\":\"access-identity-type\",\"mapRequestParamType\":\"HeaderParam\",\"type\":\"string\"},{\"agg\":false,\"requestType\":\"BodyParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"bodyParam\",\"paramList\":[],\"mapName\":\"bodyParam\",\"mapRequestParamType\":\"BodyParam\",\"type\":\"string\"},{\"agg\":false,\"requestType\":\"QueryParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"queryParam\",\"paramList\":[],\"mapName\":\"queryParam\",\"mapRequestParamType\":\"QueryParam\",\"type\":\"string\"}],\"constantPramList\":[{\"paramDesc\":\"测试常量参数\",\"requestType\":\"HeaderParam\",\"name\":\"ConstParam\",\"value\":\"123\"},{\"paramDesc\":\"测试常量参数\",\"requestType\":\"QueryParam\",\"name\":\"Z\",\"value\":\"1\"}],\"url\":\"http://10.133.65.137:8081/localfiletest/AllInOne\"}");
  }

  @Test
  public void insertApiTest1() {
    //
    String uuid = UUID.randomUUID().toString().replace("-", "");

    System.out.println(uuid);

    String apiKey = String.format(RedisKeyEnums.GATEWAY_API_URL.getKeyPattern(), "/api/test");

    redisTemplate.opsForHash().put(apiKey, "apiId", "0a142d51cebf431bb0a31f6794835edc");
    redisTemplate.opsForHash().put(apiKey, "apiClassifyId", "7f6f3a0d0d5148b2abda545acb59a9b3");
    redisTemplate.opsForHash().put(apiKey, "productClassifyId", "076c7b16eae04fd0a062ac2f76b5b461");
    redisTemplate.opsForHash().put(apiKey, "productId", "0a356fdff7584902bf689ea52867b88d");
    redisTemplate.opsForHash().put(apiKey, "name", "TestApi1");
    redisTemplate.opsForHash().put(apiKey, "useMock", false);
    redisTemplate.opsForHash().put(apiKey, "mockTimeOut", 200);
    redisTemplate.opsForHash().put(apiKey, "mockData", "{\"mockReturn\":\"message\"}");

    redisTemplate.opsForHash().put(apiKey, "bidirectionalSSL", false);
    redisTemplate.opsForHash().put(apiKey, "method", "GET");
    redisTemplate.opsForHash().put(apiKey, "serviceHttpMethod", "GET");
    redisTemplate.opsForHash().put(apiKey, "protocol", "HTTP");
    redisTemplate.opsForHash().put(apiKey, "contentType", "application/json");
    redisTemplate.opsForHash().put(apiKey, "requestProtocol", "HTTP");
    redisTemplate.opsForHash().put(apiKey, "url", "/api/test");
    redisTemplate
        .opsForHash()
        .put(apiKey, "connectUrl", "http://10.133.65.137:8082/localfiletest/AllInOne");
    redisTemplate.opsForHash().put(apiKey, "idempotent", "non");
    redisTemplate.opsForHash().put(apiKey, "bodyPassThrough", true);
    redisTemplate.opsForHash().put(apiKey, "responseResultPassThrough", true);
    redisTemplate.opsForHash().put(apiKey, "timeOut", 2000);
    redisTemplate.opsForHash().put(apiKey, "pathJoin", false);
    redisTemplate.opsForHash().put(apiKey, "pathMatch", false);
    redisTemplate.opsForHash().put(apiKey, "rpcServiceName", "");
    redisTemplate.opsForHash().put(apiKey, "rpcMethodName", "");
    redisTemplate.opsForHash().put(apiKey, "rpcServiceUrl", "");
    redisTemplate.opsForHash().put(apiKey, "healthExam", false);
    redisTemplate.opsForHash().put(apiKey, "healthStatus", true);

    redisTemplate
        .opsForHash()
        .put(
            apiKey,
            "plugin",
            "[{\"pluginId\":\"divide\",\"policyId\":\"a0fb8b75123f49d881e0ab0bd500a698\",\"name\":\"负载均衡分配插件\"},{\"pluginId\":\"httpProxy\",\"policyId\":\"a0fb8b75123f49d881e0ab0bd500a698\",\"name\":\"http代理插件\"}]");

    redisTemplate.opsForHash().put(apiKey, "routingStgy", true);

    redisTemplate
        .opsForHash()
        .put(
            apiKey,
            "routingStgyList",
            "[{\"pathJoin\":0,\"connectUrl\":\"http://10.133.65.137:8082/localfiletest/AllInOne\",\"rpcServiceName\":\"\",\"routeParamList\":[{\"gmtUpdate\":\"2021-07-20 09:23:55.000\",\"paramLocation\":\"QueryParam\",\"id\":\"3d1de54e4b954ebc9f4da67bc7a00d07\",\"paramName\":\"queryParam\",\"sort\":1,\"ytenantId\":\"75c13acc-ae53-4694-9a6e-3ddcce26d7f3\",\"gmtCreate\":\"2021-07-20 09:23:55.000\",\"apiId\":\"4b3f930f03cb488f9fd7fe4e35fdac64\",\"policyId\":\"a5ad680e121e4868ac09a97b641aae69\",\"paramCondition\":0,\"paramValue\":\"8082\"}],\"rpcMethodName\":\"\",\"sort\":1,\"ytenantId\":\"75c13acc-ae53-4694-9a6e-3ddcce26d7f3\",\"gmtCreate\":\"2021-07-20 09:23:55.000\",\"timeOut\":60,\"serviceHttpMethod\":\"POST\",\"stgyName\":\"10.133.65.137:8082\",\"effeMode\":0,\"gmtUpdate\":\"2021-07-20 09:23:55.000\",\"rpcServiceUrl\":\"\",\"id\":\"a5ad680e121e4868ac09a97b641aae69\",\"rpcAppName\":\"\",\"apiId\":\"4b3f930f03cb488f9fd7fe4e35fdac64\"},{\"pathJoin\":0,\"connectUrl\":\"http://10.133.65.137:8083/8083\",\"rpcServiceName\":\"\",\"routeParamList\":[{\"gmtUpdate\":\"2021-07-20 09:23:55.000\",\"paramLocation\":\"QueryParam\",\"id\":\"9fc12abe726049e79c368daac24d151a\",\"paramName\":\"queryParam\",\"sort\":1,\"ytenantId\":\"75c13acc-ae53-4694-9a6e-3ddcce26d7f3\",\"gmtCreate\":\"2021-07-20 09:23:55.000\",\"apiId\":\"4b3f930f03cb488f9fd7fe4e35fdac64\",\"policyId\":\"24743fdb4bda43ba8daa6774b6b7c7f0\",\"paramCondition\":0,\"paramValue\":\"8083\"}],\"rpcMethodName\":\"\",\"sort\":2,\"ytenantId\":\"75c13acc-ae53-4694-9a6e-3ddcce26d7f3\",\"gmtCreate\":\"2021-07-20 09:23:55.000\",\"timeOut\":10,\"serviceHttpMethod\":\"POST\",\"stgyName\":\"10.133.65.137:8083\",\"effeMode\":0,\"gmtUpdate\":\"2021-07-20 09:23:55.000\",\"rpcServiceUrl\":\"\",\"id\":\"24743fdb4bda43ba8daa6774b6b7c7f0\",\"rpcAppName\":\"\",\"apiId\":\"4b3f930f03cb488f9fd7fe4e35fdac64\"}]");
    redisTemplate
        .opsForHash()
        .put(
            apiKey,
            "paramList",
            "[{\"requestType\":\"HeaderParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"access-identity-value\",\"type\":\"string\",\"required\":false},{\"requestType\":\"HeaderParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"access-recognize-key\",\"type\":\"string\",\"required\":false},{\"requestType\":\"HeaderParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"access-identity-type\",\"type\":\"string\",\"required\":false},{\"requestType\":\"BodyParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"bodyParam\",\"type\":\"string\",\"required\":false},{\"requestType\":\"QueryParam\",\"array\":false,\"defaultValue\":\"\",\"name\":\"queryParam\",\"type\":\"string\",\"required\":false}]");
    redisTemplate
        .opsForHash()
        .put(
            apiKey,
            "serviceInfo",
            "{\"method\":\"POST\",\"bodyPassthrough\":true,\"paramList\":[{\"agg\":false,\"requestType\":\"HeaderParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"access-identity-value\",\"paramList\":[],\"mapName\":\"access-identity-value\",\"mapRequestParamType\":\"HeaderParam\",\"type\":\"string\"},{\"agg\":false,\"requestType\":\"HeaderParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"access-recognize-key\",\"paramList\":[],\"mapName\":\"access-recognize-key\",\"mapRequestParamType\":\"HeaderParam\",\"type\":\"string\"},{\"agg\":false,\"requestType\":\"HeaderParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"access-identity-type\",\"paramList\":[],\"mapName\":\"access-identity-type\",\"mapRequestParamType\":\"HeaderParam\",\"type\":\"string\"},{\"agg\":false,\"requestType\":\"BodyParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"bodyParam\",\"paramList\":[],\"mapName\":\"bodyParam\",\"mapRequestParamType\":\"BodyParam\",\"type\":\"string\"},{\"agg\":false,\"requestType\":\"QueryParam\",\"primitive\":false,\"array\":false,\"defaultValue\":\"\",\"name\":\"queryParam\",\"paramList\":[],\"mapName\":\"queryParam\",\"mapRequestParamType\":\"QueryParam\",\"type\":\"string\"}],\"constantPramList\":[{\"paramDesc\":\"测试常量参数\",\"requestType\":\"HeaderParam\",\"name\":\"ConstParam\",\"value\":\"123\"},{\"paramDesc\":\"测试常量参数\",\"requestType\":\"QueryParam\",\"name\":\"Z\",\"value\":\"1\"}],\"url\":\"http://10.133.65.137:8081/localfiletest/AllInOne\"}");
  }
}
