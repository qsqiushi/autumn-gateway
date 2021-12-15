package com.autumn.gateway.server.vertx.properties;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-22:15:27
 */
@Data
@Accessors(chain = true)
public class RetryConfig {

  private Integer initialSleepTime;

  private Integer maxTimes;

  public RetryConfig() {
    this.initialSleepTime = 3000;
    this.maxTimes = 3;
  }
}
