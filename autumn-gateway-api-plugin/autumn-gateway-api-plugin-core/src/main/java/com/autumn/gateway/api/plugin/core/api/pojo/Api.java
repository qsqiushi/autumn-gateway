package com.autumn.gateway.api.plugin.core.api.pojo;

import java.util.List;
import java.util.Objects;

import javax.annotation.PreDestroy;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang.StringUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-06-05 14:30 //@Accessors(chain = true) 加上会影响 BeanUtils
 */
@Data
@Slf4j
public class Api implements Reactable {
  /** api id */
  @NotEmpty(message = "apiId不能为空")
  private String apiId;
  /** API 分类ID */
  @NotEmpty(message = "API分类ID不能为空")
  private String apiClassifyId;
  /** 产品分类ID */
  @NotEmpty(message = "产品分类ID不能为空")
  private String productClassifyId;
  /** 产品ID */
  @NotEmpty(message = "产品ID不能为空")
  private String productId;
  /** api 名称 */
  @NotEmpty(message = "API名称不能为空")
  private String name;
  /** 是否是mock */
  private Boolean useMock;
  /** mock 响应时间 */
  private Long mockTimeOut;
  /** mock数据 ？ */
  private String mockData;
  /** 双向ssl */
  private Boolean bidirectionalSSL;
  /** 请求方法 */
  @NotEmpty(message = "请求方法不能为空")
  private String method;
  /** 后台服务请求方法 */
  @NotEmpty(message = "后台服务请求方法不能为空")
  private String serviceHttpMethod;
  /** 后台服务请求体是否是json数组 */
  private Boolean serviceJsonArray;
  /** 后台服务请求头 ContentType */
  @NotEmpty(message = "后台服务请求ContentType不能为空")
  private String serviceContentType;
  /** 协议 */
  @NotEmpty(message = "protocol不能为空")
  private String protocol;
  /** contentType */
  @NotEmpty(message = "contentType不能为空")
  private String contentType;
  /** 后端接入方式 */
  @NotEmpty(message = "requestProtocol不能为空")
  private String requestProtocol;
  /** api访问地址 */
  @NotEmpty(message = "url不能为空")
  private String url;
  /** 后台地址 */
  @NotEmpty(message = "connectUrl不能为空")
  private String connectUrl;
  /** 幂等性 */
  private Boolean idempotent;
  /** transparent_Transmission 请求透传 */
  private Boolean tpTx;
  /** 响应结果是否透传 */
  private Boolean responseResultPassThrough;
  /** 超长时间 单位毫秒 */
  private Long timeOut;
  /** 是否拼接子路径 */
  private Boolean pathJoin;
  /** 健康检查开关 */
  private String healthExam;
  /** 健康状态 */
  private String healthStatus;
  /**
  /** 是否采用路由策略 private String routingStgyLis
   * @see com.autumn.gateway.api.plugin.core.enums.RoutingStgyEnum
   */
  private Integer routingStgy;
  /** 请求参数 */
  private String paramList;
  /** 后台请求的常量参数 */
  private String proxyConstParamList;
  /** 插件配置 */
  @NotEmpty(message = "plugin不能为空")
  private String plugin;

  private List<AbstractProxyEndpoint> endPoints;

  private List<PluginConfigInfo> pluginInfos;

  /** 请求体是否是json数组 */
  private Boolean apiJsonArray;

  @Override
  public int hashCode() {
    return Objects.hash(this.getApiId());
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof Api) {

      Api temp = (Api) obj;

      return StringUtils.equals(temp.getApiId(), this.apiId);

    } else {
      return Boolean.FALSE;
    }
  }

  @PreDestroy
  public void destroy() {
    log.info("api destroy...");
  }
}
