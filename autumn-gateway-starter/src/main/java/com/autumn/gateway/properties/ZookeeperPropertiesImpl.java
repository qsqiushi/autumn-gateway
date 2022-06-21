package com.autumn.gateway.properties;

import com.autumn.gateway.server.vertx.properties.ZkClusterConfig;
import com.autumn.gateway.server.vertx.service.ZookeeperProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-04-28 17:41
 */
@Data
@Component
@ConfigurationProperties(prefix = "autumn.zookeeper")
public class ZookeeperPropertiesImpl implements ZookeeperProperties {

  private ZkClusterConfig clusterConfig;

  private String syncPath;
}
