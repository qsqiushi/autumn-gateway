package com.autumn.gateway.api.plugin.core.api.pojo;

import java.io.Serializable;
import java.util.List;

import com.autumn.gateway.api.plugin.core.enums.EndpointStatusEnum;
import com.autumn.gateway.api.plugin.core.enums.EndpointTypeEnum;
import com.autumn.gateway.api.plugin.core.pojo.RouteParam;

import lombok.Data;

/**
 * @program autumn-gateway
 * @description 服务节点
 * @author qiushi
 * @since 2021-06-17 10:07
 */
@Data
public abstract class AbstractProxyEndpoint implements Serializable {

	/** 默认权重 */
	private static final int DEFAULT_WEIGHT = 1;
	/** 名称 */
	private String name;
	/** 目标地址 */
	private String target;
	/** 权重 */
	private int weight = DEFAULT_WEIGHT;
	/** 是否为后备 */
	private boolean backup;
	/** 状态 */
	private EndpointStatusEnum status = EndpointStatusEnum.UP;

	/** 类型 */
	private EndpointTypeEnum type;
	/** 是否拼接子路径 /api/** 后台服务 /test 如果true调用后台服务地址为/test/** */
	private Boolean pathJoin;
	/** 条件参数 */
	private List<RouteParam> routeParamList;
	/** 顺序 */
	private int sort;
	/** 超时时间 */
	private int timeOut;
	/** 后台服务调用方法 */
	private String serviceHttpMethod;

	/** 生效方式 1满足任意条件 2满足所有条件 */
	private int effMode;

	/** rpc服务名称 */
	private String rpcServiceName;
	/** rpc服务地址 */
	private String rpcServiceUrl;
	/** rpcApp名称 */
	private String rpcAppName;

	protected AbstractProxyEndpoint() {
	}

	protected AbstractProxyEndpoint(EndpointTypeEnum type, String name, String target) {
		this.type = type;
		this.name = name;
		this.target = target;
	}
}