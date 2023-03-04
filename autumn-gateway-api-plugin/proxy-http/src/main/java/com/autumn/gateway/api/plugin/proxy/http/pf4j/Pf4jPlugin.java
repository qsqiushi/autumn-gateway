package com.autumn.gateway.api.plugin.proxy.http.pf4j;

import com.autumn.gateway.api.plugin.core.IPluginFactoryExtensionPoint;
import com.autumn.gateway.api.plugin.core.factory.IPluginFactory;
import com.autumn.gateway.api.plugin.core.service.IFailInvokerManagerService;
import com.autumn.gateway.api.plugin.proxy.http.HttpProxyPluginFactory;
import com.autumn.gateway.common.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author qiushi
 * @program agw
 * @description pf4j插件扩展
 * @since 2021-07-06:08:36
 */
@Slf4j
public class Pf4jPlugin extends SpringPlugin {

    @Override
    public void start() {
        log.info("Starting plugin {}", wrapper.getPluginId());
    }

    @Override
    public void stop() {
        log.info("Stopping plugin {}", wrapper.getPluginId());
    }

    public Pf4jPlugin(PluginWrapper wrapper) {
        super(wrapper);
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

    @Extension
    @SuppressWarnings({"all"})
    public static class HttpProxyPluginFactoryExtensionPoint implements IPluginFactoryExtensionPoint {
        private PluginWrapper wrapper;
        private IFailInvokerManagerService failInvokerManagerService;

        @Autowired
        public HttpProxyPluginFactoryExtensionPoint(PluginWrapper wrapper, IFailInvokerManagerService failInvokerManagerService) {
            this.wrapper = wrapper;
            this.failInvokerManagerService = failInvokerManagerService;
        }

        @Override
        public IPluginFactory createFactory() {
            return new HttpProxyPluginFactory(wrapper, failInvokerManagerService);
        }
    }
}
