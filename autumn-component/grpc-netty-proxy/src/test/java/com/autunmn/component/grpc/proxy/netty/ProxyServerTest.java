package com.autunmn.component.grpc.proxy.netty;

import com.autumn.component.grpc.proxy.netty.ProxyServer;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 代理测试
 * @since 2022-01-17 16:16
 */
public class ProxyServerTest {

  public static void main(String[] args) {
    ProxyServer proxyServer = new ProxyServer("localhost", 81);
    proxyServer.start();
  }
}
