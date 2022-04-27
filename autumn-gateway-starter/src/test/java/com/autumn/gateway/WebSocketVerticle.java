package com.autumn.gateway;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-01-18 19:05
 */
import io.vertx.core.*;
import io.vertx.core.http.ServerWebSocket;

import java.util.HashMap;
import java.util.Map;

public class WebSocketVerticle extends AbstractVerticle {
  // 保存每一个连接到服务器的通道
  private static Map<String, ServerWebSocket> connectionMap = new HashMap<>(16);

  public static void main(String[] args) {
    Map<ServerWebSocket, Long> webSockets = new HashMap<>();

    // 创建http服务器,并开始处理请求
    Vertx.vertx()
        .createHttpServer()
        .requestHandler(
            req -> {

              // 路径匹配
              if ("/ws".equals(req.path())) {
                System.out.println("ws conn...");
                Future<ServerWebSocket> future = req.toWebSocket();

                future.onSuccess(
                    new Handler<ServerWebSocket>() {
                      @Override
                      public void handle(ServerWebSocket webSocket) {
                        System.out.println("建立长连接成功");

                        // 处理文本数据
                        webSocket.frameHandler(
                            handler -> {
                              String textData = handler.textData();
                              webSocket.writeFinalTextFrame("服务器收到:" + textData);
                            });
                        // 处理连接关闭
                        webSocket.closeHandler(
                            handler -> {
                              System.out.println("ws close!");
                              webSockets.remove(webSocket);
                            });
                      }
                    });

                future.onComplete(
                    new Handler<AsyncResult<ServerWebSocket>>() {
                      @Override
                      public void handle(AsyncResult<ServerWebSocket> event) {
                        System.out.println("onComplete");
                      }
                    });

                future.onFailure(
                    new Handler<Throwable>() {
                      @Override
                      public void handle(Throwable event) {
                        System.out.println("建立长连接失败");
                      }
                    });
              } else {
                // 处理http请求
                req.response()
                    .putHeader("content-type", "text/plain")
                    .end("this is http request,visited:" + req.path());
              }
            })
        .listen(8888);
  }
}
