package com.autumn.gateway.core.event.enums;

/**
 * <同步消息类型>
 *
 * @author qiushi
 * @since 2021/8/17 08:51
 */
public enum SyncReactorEvent {

  /** 销毁单个API */
  UNDEPLOY_API,
  /** 刷新单个API */
  REFRESH_API,
  /** 刷新所有API */
  REFRESH_ALL_API,
  /** 刷新APP策略 */
  REFRESH_APP_POLICY,
  /** 刷新系统策略 */
  REFRESH_SYS_POLICY,
  /** 刷新产品策略 */
  REFRESH_PRODUCT_POLICY,
  /** 刷新产品分类策略 */
  REFRESH_PRODUCT_CLASSIFY_POLICY,

  /** 停止服务 */
  STOP_SERVERS,

  /** 启动服务 */
  START_SERVERS,
  /** 重新加载API插件 */
  RELOAD_API_PLUGIN,
  ;
}
