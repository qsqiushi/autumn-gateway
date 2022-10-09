package com.autumn.gateway.api.plugin.core.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author qiushi
 * @program agw-ms-be2.0
 * @description 路由参数
 * @since 2022-02-16 18:59
 */
@Data
public class RouteParam implements Serializable {

  private static final long serialVersionUID = 2405172041950251807L;

  /** 参数位置 */
  private String paramLocation;
  /** 参数名称 */
  private String paramName;
  /** 顺序 */
  private Integer sort;
  /** 条件 0等于 -1小于 1大于 */
  private Integer paramCondition;
  /** 参数值 */
  private String paramValue;
}
