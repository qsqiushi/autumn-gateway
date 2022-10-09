package com.autumn.gateway.api.plugin.divide.pojo;

import com.autumn.gateway.api.plugin.core.api.pojo.AbstractProxyEndpoint;
import com.autumn.gateway.api.plugin.core.enums.EndpointTypeEnum;

/**
 * @author qiushi
 * @program agw
 * @description 自定义后台服务节点
 * @since 2021-08-26:13:36
 */
public class UserDefinedProxyEndpoint extends AbstractProxyEndpoint {

	private static final String QUERY_SEPARATOR = "?";

	public UserDefinedProxyEndpoint(EndpointTypeEnum type, String name, String target) {
		super(type, name, target);
	}

	public UserDefinedProxyEndpoint() {
		super();
	}

	public String getTargetWithoutQueryParams() {
		String uri = getTarget();
		int targetQueryIndex = uri.indexOf(QUERY_SEPARATOR);
		if (targetQueryIndex > -1) {
			return uri.substring(0, targetQueryIndex);
		} else {
			return uri;
		}
	}
}
