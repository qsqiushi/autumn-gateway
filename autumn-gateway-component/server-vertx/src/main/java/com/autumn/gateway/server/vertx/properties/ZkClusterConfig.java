package com.autumn.gateway.server.vertx.properties;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2021-12-15 11:01
 */
@Data
@Accessors(chain = true)
public class ZkClusterConfig {

  private String zookeeperHosts;

  private String rootPath;

  private RetryConfig retry;
}
