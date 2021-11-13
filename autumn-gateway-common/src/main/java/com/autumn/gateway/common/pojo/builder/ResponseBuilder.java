package com.autumn.gateway.common.pojo.builder;

import com.autumn.gateway.common.enums.CommonEnum;
import com.autumn.gateway.common.enums.ResultCode;
import com.autumn.gateway.common.pojo.BaseResponse;
import io.vertx.core.json.Json;

/**
 * <响应builder>
 *
 * @author qiushi
 * @since 2021/7/29 14:16
 */
public class ResponseBuilder {

  /**
   * <响应成功>
   *
   * @return
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/29 14:16
   */
  public static <T> BaseResponse<T> success() {

    return buildResponse(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getName(), null);
  }

  /**
   * 响应成功
   *
   * @param data 数据
   * @return
   */
  public static <T> BaseResponse<T> success(T data) {
    return buildResponse(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getName(), data);
  }

  /**
   * 响应成功
   *
   * @return
   */
  public static String successStr() {
    return Json.encode(success());
  }

  /**
   * 响应成功
   *
   * @param data
   * @return
   */
  public static String successStr(Object data) {
    return Json.encode(success(data));
  }

  public static <T> BaseResponse<T> badRequest() {
    return buildResponse(ResultCode.BAD_REQUEST.getCode(), ResultCode.BAD_REQUEST.getName(), null);
  }

  public static <T> BaseResponse<T> badRequest(String message) {
    return buildResponse(ResultCode.BAD_REQUEST.getCode(), message, null);
  }

  public static <T> BaseResponse<T> notFind() {
    return buildResponse(ResultCode.NOT_FIND.getCode(), ResultCode.NOT_FIND.getName(), null);
  }

  public static <T> BaseResponse<T> notFind(String val) {
    return buildResponse(ResultCode.NOT_FIND.getCode(), val, null);
  }

  public static <T> BaseResponse<T> serverError() {
    return buildResponse(
        ResultCode.SVR_INNER_ERROR.getCode(), ResultCode.SVR_INNER_ERROR.getName(), null);
  }

  /**
   * 失败响应
   *
   * @param resultCode
   * @param <T>
   * @return
   */
  public static <T> BaseResponse<T> fail(CommonEnum resultCode) {
    return buildResponse(resultCode.getCode(), resultCode.getName(), null);
  }

  /**
   * 失败响应
   *
   * @param resultCode
   * @return
   */
  public static String failStr(CommonEnum resultCode) {
    return Json.encode(fail(resultCode.getCode(), resultCode.getName()));
  }

  /**
   * 失败响应
   *
   * @param resultCode
   * @return
   */
  public static String failStr(Integer resultCode, String msg) {
    return Json.encode(fail(resultCode, msg));
  }

  /**
   * 失败响应
   *
   * @param code
   * @param msg
   * @return
   */
  public static <T> BaseResponse<T> fail(int code, String msg) {
    return buildResponse(code, msg, null);
  }

  /**
   * 失败响应
   *
   * @param code
   * @param msg
   * @return
   */
  public static String failStr(int code, String msg) {
    return Json.encode(fail(code, msg));
  }

  /**
   * 构建响应
   *
   * @param code
   * @param msg
   * @param data
   * @return
   */
  public static <T> BaseResponse<T> buildResponse(int code, String msg, T data) {
    if (data != null) {
      return new BaseResponse<T>(code, msg, data);
    }
    return new BaseResponse<T>(code, msg);
  }

  public static String buildResponseJson(int code, String msg) {
    return Json.encode(buildResponse(code, msg, null));
  }

  public static String buildResponseJson(int code, String msg, Object data) {
    return Json.encode(buildResponse(code, msg, data));
  }

  /**
   * <判断结果是否成功>
   *
   * @param response
   * @return : boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/29 14:36
   */
  public static <T> boolean isSuccess(BaseResponse<T> response) {
    /**
     * <>
     *
     * @param response 响应
     * @return : boolean
     * @author qiushi
     * @updator qiushi
     * @since 2021/7/29 14:36
     */
    return response != null && response.getCode() == ResultCode.SUCCESS.getCode();
  }

  /**
   * <判断结果是否不成功>
   *
   * @param response 响应
   * @return : boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/7/29 14:35
   */
  public static <T> boolean isNotSuccess(BaseResponse<T> response) {

    return response == null || response.getCode() != ResultCode.SUCCESS.getCode();
  }
}
