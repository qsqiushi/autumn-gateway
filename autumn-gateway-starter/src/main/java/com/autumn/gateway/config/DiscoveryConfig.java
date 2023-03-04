package com.autumn.gateway.config;

import io.vertx.core.Vertx;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2021-08-25 09:56
 */
@Slf4j
@Component
public class DiscoveryConfig {
    private Vertx vertx;

    @Autowired
    public DiscoveryConfig(Vertx vertx) {
        this.vertx = vertx;
    }


    /**
     * <预留 Vert.x 提供了一个服务发现的基础组件，用来发布和发现各种类型的资源，比如服务代理、HTTP端点（endpoint）、数据源（data source）等等。>
     *
     * @return io.vertx.servicediscovery.ServiceDiscovery
     * @author qiushi
     * @updator qiushi
     * @since 2021/12/17 15:59
     */
    @Bean
    public ServiceDiscovery get() {

        // Customize the configuration
        return
                ServiceDiscovery.create(
                        vertx,
                        new ServiceDiscoveryOptions()
                                .setAnnounceAddress("service-announce")
                                .setName("my-name"));

        // Do something...

        // discovery.close();


    }
}
