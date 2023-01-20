package com.autumn.gateway.common.pojo;

import com.autumn.gateway.common.enums.CommonEnum;
import com.autumn.gateway.common.enums.ResultCode;


import java.io.Serializable;
import java.util.Objects;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-29 14:08
 */
public class BaseResponse<T> implements BaseResult<T>, Serializable {

  /** 错误码 */
  private int code;

  /** 错误信息 */
  private String msg;

  /** 数据 */
  private T data;

  @Override
  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  @Override
  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public BaseResponse() {}

  public BaseResponse(CommonEnum resultCode) {
    this.code = resultCode.getCode();
    this.msg = resultCode.getName();
  }

  public BaseResponse(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public BaseResponse(int code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public BaseResponse(T data) {
    this.code = ResultCode.SUCCESS.getCode();
    this.msg = ResultCode.SUCCESS.getName();
    this.data = data;
  }

  public boolean resultDataIsNull() {
    return Objects.isNull(this.data);
  }

  public boolean resultDataNotNull() {
    return Objects.nonNull(this.data);
  }
}
