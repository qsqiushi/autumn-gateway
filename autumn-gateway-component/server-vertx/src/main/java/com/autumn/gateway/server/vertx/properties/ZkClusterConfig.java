package com.autumn.gateway.server.vertx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-12-15 11:01
 */
@Data
@Component
@PropertySource(value = {"classpath:zk-vertx.properties"})
@ConfigurationProperties(prefix = "zk")
public class ZkClusterConfig {

  private String zookeeperHosts;

  private String rootPath;

  private RetryConfig retry;
}
