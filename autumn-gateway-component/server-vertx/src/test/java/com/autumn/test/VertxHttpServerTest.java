package com.autumn.test;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 证明httpserver 可以close 然后在启动 阿斯顿发
 * @since create 2021-12-15 16:29
 */
public class VertxHttpServerTest {

  public static void main(String[] args) throws InterruptedException {

    Vertx vertx = Vertx.vertx();
    HttpServer httpServer = vertx.createHttpServer();

    httpServer
        .requestHandler(httpServerRequest -> System.out.println("123"))
        .exceptionHandler(
            ex -> {
              ex.printStackTrace();
            })
        .listen(
            80,
            server -> {
              if (server.succeeded()) {
                System.out.println("success");
              } else {
                server.cause().printStackTrace();
              }
            });

    Thread.sleep(5000);

    httpServer.close();

    Thread.sleep(5000);

    httpServer
        .requestHandler(httpServerRequest -> System.out.println("123"))
        .exceptionHandler(
            ex -> {
              ex.printStackTrace();
            })
        .listen(
            81,
            server -> {
              if (server.succeeded()) {
                System.out.println("success");
              } else {
                server.cause().printStackTrace();
              }
            });
  }
}
