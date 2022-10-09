package com.autumn.gateway.common.enums;

import lombok.Getter;

/** @author qiushi */
@Getter
public enum ResultCode implements CommonEnum<ResultCode> {
  /**
   * 修复异味
   * public enum ResultCode implements CommonEnum<ResultCode>
   * @author 李鹤
   * @time 2022/7/1 13:25
   */

  /** 成功 */
  SUCCESS(200, "成功"),
  /** 请求参数不合理或为空 */
  BAD_REQUEST(400, "请求参数不合理或为空"),
  /** 用户登录未登录或已过期，请重新登录 */
  NOT_LOGIN(401, "用户登录未登录或已过期，请重新登录"),
  /** 没有权限操作 */
  NOT_FORBIDDEN(403, "没有权限操作"),
  /** 没有找到对应信息 */
  NOT_FIND(404, "没有找到对应信息"),
  /** 当期状态不支持此类型操作 */
  NOT_SUPPORT_OPT(409, "当期状态不支持此类型操作"),
  /** 系统错误 */
  SVR_INNER_ERROR(500, "系统错误"),
  /** 请求调用失败 */
  GATEWAY_ERROR(1018, "请求调用失败"),
  /** 访问过快 */
  GATEWAY_LIMIT_ERROR(1019, "访问过快"),

  /** 错误的ContentType */
  BAD_CONTENT_TYPE(1020, "错误的ContentType"),

  BAD_HTTP_METHOD(1021, "错误的HttpMethod"),

  BAD_HTTP_PROTOCOL(1022, "错误的协议"),

  /** 系统错误 */
  SVR_INNER_ERROR_NOT_RESULT(1023, "系统错误,没有响应结果"),
  ;

  /** code */
  private final int code;
  /** 名称 */
  private final String name;

  private ResultCode(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public static ResultCode valueOf(int code) {
    for (ResultCode type : ResultCode.values()) {
      if (type.getCode() == code) {
        return type;
      }
    }
    return SVR_INNER_ERROR;
  }
}
