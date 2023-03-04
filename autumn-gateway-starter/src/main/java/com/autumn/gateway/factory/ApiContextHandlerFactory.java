package com.autumn.gateway.factory;

import com.autumn.gateway.api.plugin.core.api.handler.IApiHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.factory.AbstractPluginFactory;
import com.autumn.gateway.core.classloader.ReactorHandlerClassLoader;
import com.autumn.gateway.core.handler.ReactorHandlerManager;
import com.autumn.gateway.core.processor.provider.PluginChainProvider;
import com.autumn.gateway.service.IApiContextManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2021-07-27 19:00
 */
@Slf4j
@Component
public class ApiContextHandlerFactory {

    private ApplicationContext applicationContext;
    private ReactorHandlerManager reactorHandlerManager;
    private IApiContextManagerService apiContextManagerService;

    @Autowired
    public ApiContextHandlerFactory(ApplicationContext applicationContext, ReactorHandlerManager reactorHandlerManager, IApiContextManagerService apiContextManagerService) {
        this.applicationContext = applicationContext;
        this.reactorHandlerManager = reactorHandlerManager;
        this.apiContextManagerService = apiContextManagerService;
    }

    public IApiHandler create(Api api) {

        // 创建API ApiHandler的上下文
        AbstractApplicationContext internalApplicationContext = createApplicationContext(api);
        IApiHandler handler = internalApplicationContext.getBean(IApiHandler.class);
        reactorHandlerManager.register(handler);
        return handler;
    }

    private AbstractApplicationContext createApplicationContext(Api api) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setParent(applicationContext);
        context.setClassLoader(new ReactorHandlerClassLoader(applicationContext.getClassLoader()));
        context.setEnvironment((ConfigurableEnvironment) applicationContext.getEnvironment());

        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setEnvironment(applicationContext.getEnvironment());
        context.addBeanFactoryPostProcessor(configurer);

        // 注册API
        context.getBeanFactory().registerSingleton("api", api);
        // 注册插件链提供者
        context.register(PluginChainProvider.class);

        Map<String, AbstractPluginFactory> apiPluginFactoryMap =
                applicationContext.getBeansOfType(AbstractPluginFactory.class);
        /**
         * 扫描API相关包 此处是因为考虑到重复插件、不同顺序、不同配置 所以没有通过注册的形式实现插件实例
         * apiPluginFactoryMap.values().forEach(factory -> context.register(factory.getPluginClass()))
         */
        context.scan("com.autumn.gateway.starter.api");

        context.setId("context-api-" + api.getApiId());
        context.refresh();

        apiContextManagerService.register(api, context);

        return context;
    }
}
