package com.autumn.gateway.api.plugin.divide.pf4j;

import com.autumn.gateway.api.plugin.core.IPluginFactoryExtensionPoint;
import com.autumn.gateway.api.plugin.core.factory.IPluginFactory;
import com.autumn.gateway.api.plugin.core.pf4j.AbstractSpringPlugin;
import com.autumn.gateway.api.plugin.divide.DividePluginFactory;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;

import javax.annotation.Resource;

/**
 * @program agw
 * @description pf4j插件扩展
 * @author qiushi
 * @since 2021-07-06:08:36
 */
@Slf4j
public class DividePf4jPlugin extends AbstractSpringPlugin {

	public DividePf4jPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Extension
	public static class DividePluginFactoryExtensionPoint implements IPluginFactoryExtensionPoint {

		@Resource
		private PluginWrapper wrapper;

		@Override
		public IPluginFactory createFactory() {

			return new DividePluginFactory(wrapper);
		}
	}
}
