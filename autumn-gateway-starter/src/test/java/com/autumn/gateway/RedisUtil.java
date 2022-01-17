//package com.autumn.test;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import org.springframework.core.env.MapPropertySource;
//import org.springframework.data.redis.connection.*;
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericToStringSerializer;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @program  autumn
// * @description
// * @author qiushi
// * @since 2021-07-26:10:30
// */
//public class RedisUtil {
//
//  /** 配置lettuce连接池 */
//  private static GenericObjectPoolConfig redisPool(LettuceConfig lettuceConfig) {
//
//    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//
//    if (lettuceConfig != null) {
//      config.setMaxIdle(lettuceConfig.getMaxIdle());
//      config.setMaxWaitMillis(lettuceConfig.getMaxWait());
//      config.setMinIdle(lettuceConfig.getMinIdle());
//      config.setMaxTotal(lettuceConfig.getMaxTotal());
//    }
//
//    return config;
//  }
//
//  /** 配置第一个数据源的 */
//  private static RedisClusterConfiguration redisClusterConfig(String nodes, String password) {
//    Map<String, Object> source = new HashMap<>(8);
//    source.put("spring.redis.cluster.nodes", nodes);
//    RedisClusterConfiguration redisClusterConfiguration;
//    redisClusterConfiguration =
//        new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
//    redisClusterConfiguration.setPassword(password);
//    return redisClusterConfiguration;
//  }
//
//  private static RedisStandaloneConfiguration redisClusterConfig(
//      String host, Integer port, String password, Integer database) {
//    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//    if (!StringUtils.isEmpty(password)) {
//      redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
//    }
//    redisStandaloneConfiguration.setPort(port);
//    redisStandaloneConfiguration.setHostName(host);
//    redisStandaloneConfiguration.setDatabase(database);
//    return redisStandaloneConfiguration;
//  }
//
//  private static LettuceConnectionFactory lettuceConnectionFactory(
//      GenericObjectPoolConfig redisPool, RedisConfiguration redisConfiguration) {
//    LettuceClientConfiguration clientConfiguration =
//        LettucePoolingClientConfiguration.builder().poolConfig(redisPool).build();
//    return new LettuceConnectionFactory(redisConfiguration, clientConfiguration);
//  }
//
//  private static RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//    return getRedisTemplate(redisConnectionFactory);
//  }
//
//  private static RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
//    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//    redisTemplate.setConnectionFactory(factory);
//    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
//        new Jackson2JsonRedisSerializer(Object.class);
//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//
//    // instead of  objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
//    objectMapper.activateDefaultTyping(
//        LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//
//    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//    // 设置key和value的序列化规则
//    redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Object.class));
//    redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
//    // 设置hashKey和hashValue的序列化规则
//    redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(Object.class));
//    redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
//    // 设置支持事务
//    redisTemplate.setEnableTransactionSupport(true);
//
//    redisTemplate.afterPropertiesSet();
//    return redisTemplate;
//  }
//
//  public static RedisTemplate getRedisTemplate(RedisConfig redis) {
//
//    List<String> nodes = redis.getNodes();
//
//    // 单节点redis
//    if (nodes.size() == 1) {
//      // redis 地址
//      String node = nodes.get(0);
//      // 获得host port
//      String[] strArray = node.split(":");
//      RedisStandaloneConfiguration redisStandaloneConfiguration =
//          redisClusterConfig(
//              strArray[0], Integer.parseInt(strArray[1]), redis.getPassword(), redis.getDatabase());
//      LettuceConnectionFactory lettuceConnectionFactory =
//          lettuceConnectionFactory(redisPool(redis.getLettuce()), redisStandaloneConfiguration);
//      lettuceConnectionFactory.afterPropertiesSet();
//      RedisTemplate redisTemplate = redisTemplate(lettuceConnectionFactory);
//      return redisTemplate;
//    }
//    // TODO redis 集群 redis 哨兵
//
//    return null;
//  }
//
//  public static RedisTemplate test() {
//    //
//
//    List<String> nodes = new ArrayList<>();
//    nodes.add("10.133.92.157:6379");
//    RedisStandaloneConfiguration redisStandaloneConfiguration =
//        redisClusterConfig("10.133.92.157", 6379, "redis@2021", 0);
//    LettuceConnectionFactory lettuceConnectionFactory =
//        lettuceConnectionFactory(redisPool(null), redisStandaloneConfiguration);
//    lettuceConnectionFactory.afterPropertiesSet();
//    RedisTemplate redisTemplate = redisTemplate(lettuceConnectionFactory);
//
//    System.out.println(redisTemplate.hasKey("agw:system:servers"));
//
//    return redisTemplate;
//  }
//}
