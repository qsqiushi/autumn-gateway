package com.autumn.gateway.listener;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.core.event.Event;
import com.autumn.gateway.core.event.EventListener;
import com.autumn.gateway.core.event.enums.ApiReactorEvent;
import com.autumn.gateway.factory.ApiContextHandlerFactory;
import com.autumn.gateway.service.IApiContextManagerService;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description api注册监听
 * @since 2021-07-27 15:32
 */
@Slf4j
@Component
public class ApiReactorEventListener implements EventListener<ApiReactorEvent, Api> {


    private ApiContextHandlerFactory apiContextHandlerFactory;

    private IApiContextManagerService apiContextManagerService;

    @Autowired
    public ApiReactorEventListener(ApiContextHandlerFactory apiContextHandlerFactory, IApiContextManagerService apiContextManagerService) {
        this.apiContextHandlerFactory = apiContextHandlerFactory;
        this.apiContextManagerService = apiContextManagerService;
    }

    @Override
    public void onEvent(Event<ApiReactorEvent, Api> event) {

        final Api api = event.content();

        switch (event.type()) {
            case DEPLOY:
                Future.future(
                                deployEvent -> {
                                    lookupForServiceDiscovery(api);
                                    deployEvent.complete();
                                })
                        .onComplete(
                                result -> {
                                    if (result.succeeded()) {
                                        event.getSuccessHandler();
                                    } else {
                                        event.getFailureHandler();
                                    }
                                });

                break;
            case UNDEPLOY:
                Future.future(
                                undeployEvent -> {
                                    stopServiceDiscovery(api);
                                    undeployEvent.complete();
                                })
                        .onSuccess(event.getSuccessHandler())
                        .onFailure(event.getFailureHandler());
                break;
            case UPDATE:
                Future.future(
                                updateEvent -> {
                                    stopServiceDiscovery(api);
                                    lookupForServiceDiscovery(api);
                                    updateEvent.complete();
                                })
                        .onSuccess(event.getSuccessHandler())
                        .onFailure(event.getFailureHandler());

                break;
            default:
                log.error("illegal event");
        }
    }

    /**
     * <发布API>
     *
     * @param api api
     * @author qiushi
     * @updator qiushi
     * @since 2021/7/27 15:35
     */
    private void lookupForServiceDiscovery(Api api) {
        log.info("create api [{}] handler begin...", api.getUrl());
        apiContextHandlerFactory.create(api);
        log.info("create api handler end");
    }

    /**
     * <停止服务>
     *
     * @param api api
     * @author qiushi
     * @updator qiushi
     * @see com.autumn.gateway.starter.api.handler.ApiHandlerImpl#destroy
     * @since 2021/7/27 15:36
     */
    private void stopServiceDiscovery(Api api) {
        log.info("api[{}] context begin to close ", api.getUrl());
        AbstractApplicationContext context = apiContextManagerService.getApplicationContext(api);
        context.close();
        log.info("api[{}] context close ", api.getUrl());
    }
}
