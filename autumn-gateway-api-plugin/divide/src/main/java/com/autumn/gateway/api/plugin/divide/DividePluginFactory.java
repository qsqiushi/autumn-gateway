package com.autumn.gateway.api.plugin.divide;

import org.pf4j.PluginWrapper;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.factory.AbstractPluginFactory;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;

import lombok.extern.slf4j.Slf4j;

/**
 * @author qiushi
 * @program agw
 * @description divide插件工厂
 * @since 2021-08-20:14:01
 */
@Slf4j
public class DividePluginFactory extends AbstractPluginFactory {

	public DividePluginFactory(PluginWrapper wrapper) {
		super(wrapper);
	}

	/**
	 * <获取插件类型>
	 *
	 * @return : java.lang.Class
	 * @author qiushi
	 * @updater qiushi
	 * @since 2021/6/18 16:46
	 */
	@Override
	public Class getPluginClass() {
		return DividePlugin.class;
	}

	/**
	 * <创建插件>
	 *
	 * @param pluginParam
	 *            插件参数
	 * @return : com.qm.plugin.api.IPlugin
	 * @author qiushi
	 * @updater qiushi
	 * @since 2021/6/18 16:22
	 */
	@Override
	public IPlugin create(PluginParam pluginParam) {
		return new DividePlugin(pluginParam);
	}

	/**
	 * <获取插件配置说明>
	 *
	 * @return : java.lang.String
	 * @author qiushi
	 * @updater qiushi
	 * @since 2021/7/7 08:53
	 */
	@Override
	public String getPluginConfigDes() {
		return null;
	}

	/**
	 * <销毁之前调用>
	 *
	 * @author qiushi
	 * @updater qiushi
	 * @since 2021/9/14 15:58
	 */
	@Override
	public void beforeDestroy() {
		log.info("before DividePluginFactory Destroy");
	}
}
