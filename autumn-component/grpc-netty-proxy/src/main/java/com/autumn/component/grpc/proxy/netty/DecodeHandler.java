package com.autumn.component.grpc.proxy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.*;

import static io.netty.handler.logging.LogLevel.INFO;

@ChannelHandler.Sharable
class DecodeHandler extends ChannelDuplexHandler {

  private static final Http2FrameLogger HTTP2_FRAME_LOGGER =
      new Http2FrameLogger(INFO, DecodeHandler.class);

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf byteBuf = (ByteBuf) msg;
    ByteBuf copy = byteBuf.copy();
    if (byteBuf.getByte(3) == 1) {
      Http2FrameReader reader =
          new Http2InboundFrameLogger(new DefaultHttp2FrameReader(), HTTP2_FRAME_LOGGER);
      reader.readFrame(
          ctx,
          byteBuf,
          new Http2FrameAdapter() {
            @Override
            public void onHeadersRead(
                ChannelHandlerContext ctx,
                int streamId,
                Http2Headers headers,
                int streamDependency,
                short weight,
                boolean exclusive,
                int padding,
                boolean endStream)
                throws Http2Exception {
              System.out.println(headers);
            }
          });
      byteBuf.release();
    }
    ctx.fireChannelRead(copy);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.fireChannelReadComplete();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
  }
}
