package com.autumn.component.grpc.proxy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http2.Http2Headers;

import java.net.InetSocketAddress;

/**
 * ProxyFeature
 * Created by xieyz on 16-10-8.
 */
public interface ProxyFeature {

    InetSocketAddress decide(Http2Headers headers,ByteBuf data);
}
