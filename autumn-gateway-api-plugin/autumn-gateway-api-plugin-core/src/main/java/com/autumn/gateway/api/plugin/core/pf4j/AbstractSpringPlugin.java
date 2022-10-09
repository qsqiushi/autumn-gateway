package com.autumn.gateway.api.plugin.core.pf4j;

import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.autumn.gateway.common.util.ApplicationContextUtil;

/**
 * @author qiushi
 * @program agw
 * @description
 * @since 2022-06-06 10:20
 */
public abstract class AbstractSpringPlugin extends SpringPlugin {

	@Override
	public void start() {
		log.info("Starting plugin {}", wrapper.getPluginId());
	}

	@Override
	public void stop() {
		log.info("Stopping plugin {}", wrapper.getPluginId());
	}

	@Override
	protected ApplicationContext createApplicationContext() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.setClassLoader(this.getWrapper().getPluginClassLoader());
		applicationContext.getBeanFactory().registerSingleton("wrapper", wrapper);
		applicationContext.setParent(ApplicationContextUtil.getApplicationContext());
		applicationContext.refresh();
		return applicationContext;
	}

	public AbstractSpringPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}
}
