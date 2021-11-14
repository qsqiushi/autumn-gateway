package com.autumn.gateway.starter.biz.factory;

import com.autumn.gateway.api.plugin.core.api.handler.IApiHandler;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.factory.AbstractPluginFactory;
import com.autumn.gateway.core.classloader.ReactorHandlerClassLoader;
import com.autumn.gateway.core.handler.ReactorHandlerManager;
import com.autumn.gateway.core.processor.provider.PluginChainProvider;
import com.autumn.gateway.starter.biz.service.IApiContextManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @program autumn-gateway
 * @description
 * @author qiushi
 * @since 2021-07-27:19:00
 */
@Slf4j
@Component
public class ApiContextHandlerFactory {

  @Resource private ApplicationContext bizApplicationContext;

  @Resource private ReactorHandlerManager reactorHandlerManager;

  @Resource private IApiContextManagerService apiContextManagerService;

  public IApiHandler create(Api api) {

    // 创建API ApiHandler的上下文
    AbstractApplicationContext internalApplicationContext = createApplicationContext(api);
    IApiHandler handler = internalApplicationContext.getBean(IApiHandler.class);
    reactorHandlerManager.register(handler);
    return handler;
  }

  private AbstractApplicationContext createApplicationContext(Api api) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.setParent(bizApplicationContext);
    context.setClassLoader(new ReactorHandlerClassLoader(bizApplicationContext.getClassLoader()));
    context.setEnvironment((ConfigurableEnvironment) bizApplicationContext.getEnvironment());

    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setIgnoreUnresolvablePlaceholders(true);
    configurer.setEnvironment(bizApplicationContext.getEnvironment());
    context.addBeanFactoryPostProcessor(configurer);

    // 注册API
    context.getBeanFactory().registerSingleton("api", api);
    // 注册插件链提供者
    context.register(PluginChainProvider.class);

    Map<String, AbstractPluginFactory> apiPluginFactoryMap =
        bizApplicationContext.getBeansOfType(AbstractPluginFactory.class);

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
