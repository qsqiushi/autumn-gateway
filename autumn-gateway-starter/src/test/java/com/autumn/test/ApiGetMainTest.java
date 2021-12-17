//package com.autumn.test;
//
//import com.qm.agw.common.redis.util.RedisUtil;
//import com.qm.agw.core.plugin.api.pojo.Api;
//import com.qm.agw.pluggable.api.discover.redis.enums.RedisKeyEnums;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.Map;
//import java.util.Set;
//
///**
// * @program agw
// * @description
// * @author qiushi
//* @since 2021-07-27:10:21
// */
//public class ApiGetMainTest {
//
//  public static void main(String[] args) throws Exception {
//    //
//
//    RedisTemplate<String, String> redisTemplate = RedisUtil.test();
//    Set<String> apiKeys = redisTemplate.keys(RedisKeyEnums.GATEWAY_API_URL.getPrefix().concat("*"));
//
//    for (String key : apiKeys) {
//
//      HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
//
//      Map<String, String> objectMap = hashOperations.entries(key);
//
//      Api api = new Api();
//
//      mapToObject(objectMap, api);
//
//      System.out.println("apiId:" + api.getApiId());
//    }
//  }
//
//  public static Object mapToObject(Map<String, String> map, Object obj) throws Exception {
//    if (map == null) {
//      return null;
//    }
//
//    org.apache.commons.beanutils.BeanUtils.populate(obj, map);
//
//    return obj;
//  }
//
//  public static Map<?, ?> objectToMap(Object obj) {
//    if (obj == null) return null;
//
//    return new org.apache.commons.beanutils.BeanMap(obj);
//  }
//}
