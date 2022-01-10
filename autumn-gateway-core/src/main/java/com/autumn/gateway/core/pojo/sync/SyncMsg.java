package com.autumn.gateway.core.pojo.sync;

import com.autumn.gateway.core.event.enums.SyncReactorEvent;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since  2021-08-17 08:49
 */
@Data
@Accessors(chain = true)
public class SyncMsg implements Serializable {

  private SyncReactorEvent msgType;

  private String apiUrl;

  private String bizId;

  private Pf4jPluginInfo pf4jPluginInfo;
}
