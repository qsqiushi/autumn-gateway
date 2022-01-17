package com.autumn.component.grpc.proxy.netty.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-01-17 16:49
 */
@Getter
@Setter
public class AddressBO {

  private String host;

  private Integer port;
}
