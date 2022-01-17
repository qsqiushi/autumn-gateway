package com.autumn.component.grpc.proxy.netty;

import com.autumn.component.grpc.proxy.netty.bo.AddressBO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyServer {

  private final int port;

  private final String host;

  private final EventLoopGroup group;
  private Channel channel;

  public ProxyServer(String host, int port) {
    this.host = host;
    this.port = port;
    group = new NioEventLoopGroup();
  }

  public void start() {

    AddressBO addressBO = new AddressBO();

    addressBO.setHost("localhost");
    addressBO.setPort(81);

    List<AddressBO> servers = new ArrayList<>();
    servers.add(addressBO);

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.option(ChannelOption.SO_BACKLOG, 1024);
      b.group(group)
          .channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(new ProxyServerInitializer(servers, new AtomicInteger(0)));
      channel = b.bind(port).sync().channel();
      System.out.println("Proxy server start success");
      channel.closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void awaitTermination() {}

  public void stop() {
    group.shutdownGracefully();
  }
}
