package com.autumn.gateway.common.pojo;

/**
 * 公共响应类
 *
 * @author guoxin
 * @since 2019-05-30
 */
public interface BaseResult<T> {

  T getData();

  int getCode();

  String getMsg();
}
