package com.autumn.gateway.api.plugin.core.api.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Objects;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-06-05:14:30 //@Accessors(chain = true) 加上会影响 BeanUtils
 */
@Data
@Slf4j
public class Api implements Reactable {

  private String apiId;
  private String apiClassifyId;
  private String productClassifyId;
  private String productId;
  private String name;
  private String useMock;
  private String mockTimeOut;
  private String mockData;
  private String bidirectionalSSL;
  private String method;
  private String serviceHttpMethod;
  private String protocol;
  private String contentType;
  private String requestProtocol;
  private String url;
  private String connectUrl;
  private String idempotent;
  private String bodyPassThrough;
  private String responseResultPassThrough;
  private String timeOut;
  private String pathJoin;
  private String pathMatch;
  private String rpcServiceName;
  private String rpcMethodName;
  private String rpcServiceUrl;
  private String healthExam;
  private String healthStatus;
  private String handleChain;
  private String routingStgy;
  private String routingStgyList;
  private String paramList;
  private String serviceInfo;

  private String plugin;

  private List<AbstractProxyEndpoint> endPoints;

  private List<PluginConfigInfo> pluginInfos;

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
