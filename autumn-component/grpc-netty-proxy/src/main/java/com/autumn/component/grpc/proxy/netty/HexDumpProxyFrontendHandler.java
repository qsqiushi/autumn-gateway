package com.autumn.component.grpc.proxy.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

class HexDumpProxyFrontendHandler extends ChannelInboundHandlerAdapter {

  private static final String remoteHost = "127.0.0.1";
  private static final int remotePort = 8080;

  // As we use inboundChannel.eventLoop() when buildling the Bootstrap this does not need to be
  // volatile as
  // the outboundChannel will use the same EventLoop (and therefore Thread) as the inboundChannel.
  private Channel outboundChannel;
  private Bootstrap b = new Bootstrap();
  private Channel inboundChannel;

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    System.out.println("channel active");
    inboundChannel = ctx.channel();

    // Start the connection attempt.
    b.group(inboundChannel.eventLoop())
        .channel(ctx.channel().getClass())
        .handler(new HexDumpProxyBackendHandler(inboundChannel))
        .option(ChannelOption.AUTO_READ, false);
    ChannelFuture f = b.connect(remoteHost, remotePort);
    outboundChannel = f.channel();
    f.addListener(
        new ChannelFutureListener() {
          @Override
          public void operationComplete(ChannelFuture future) {
            System.out.println("channelActive operationComplete");
            if (future.isSuccess()) {
              // connection complete start to read first data
              inboundChannel.read();
            } else {
              // Close the connection if the connection attempt has failed.
              inboundChannel.close();
            }
          }
        });
  }

  @Override
  public void channelRead(final ChannelHandlerContext ctx, Object msg) {
    System.out.println("channel channelRead");
    if (outboundChannel == null) {
      return;
    }

    if (outboundChannel.isActive()) {
      outboundChannel
          .writeAndFlush(msg)
          .addListener(
              new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                  if (future.isSuccess()) {
                    // was able to flush out data, start to read the next chunk
                    ctx.channel().read();
                  } else {
                    future.channel().close();
                  }
                }
              });
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    System.out.println("Front channelInactive");
    if (outboundChannel != null) {
      closeOnFlush(outboundChannel);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    closeOnFlush(ctx.channel());
  }

  /** Closes the specified channel after all queued write requests are flushed. */
  static void closeOnFlush(Channel ch) {
    if (ch.isActive()) {
      ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
  }
}
