package com.autumn.gateway.starter.biz.config;

import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import io.vertx.core.Vertx;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-08-25:09:56
 */
@Slf4j
@Component
public class DiscoveryConfig {

  @Resource private IVertxManagerService vertxManagerService;

  @Bean
  private Vertx getVertx() {
    return vertxManagerService.getVertx();
  }

  @Bean
  public ServiceDiscovery get() {

    Vertx vertx = vertxManagerService.getVertx();

    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    // Customize the configuration
    discovery =
        ServiceDiscovery.create(
            vertx,
            new ServiceDiscoveryOptions()
                .setAnnounceAddress("service-announce")
                .setName("my-name"));

    // Do something...

    // discovery.close();

    return discovery;
  }
}
