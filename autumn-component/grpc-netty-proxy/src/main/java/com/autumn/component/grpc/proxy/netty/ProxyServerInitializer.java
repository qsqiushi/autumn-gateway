package com.autumn.component.grpc.proxy.netty;

import com.autumn.component.grpc.proxy.netty.bo.AddressBO;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class ProxyServerInitializer extends ChannelInitializer<SocketChannel> {

  private List<AddressBO> servers;

  private final AtomicInteger counter;

  public ProxyServerInitializer(List<AddressBO> servers, AtomicInteger counter) {
    this.servers = servers;
    this.counter = counter;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ch.pipeline()
        // 在ChannelPipeline的末尾添加ChannelHandler
        .addLast(
            new LoggingHandler(LogLevel.INFO),
            new DecodeHandler(),
            new HexDumpProxyFrontendHandler());
  }
}
