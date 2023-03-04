package com.autumn.gateway.config;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.core.enums.Pf4jPluginTypeEnum;
import com.autumn.gateway.core.event.EventManager;
import com.autumn.gateway.core.event.enums.ApiReactorEvent;
import com.autumn.gateway.core.pojo.sync.Pf4jPluginInfo;
import com.autumn.gateway.core.server.IServerManager;
import com.autumn.gateway.core.service.discover.IApiDiscovererService;
import com.autumn.gateway.core.service.register.IApiRegisterService;
import com.autumn.gateway.service.IApiContextManagerService;
import com.autumn.gateway.service.IPf4jPluginManagerService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Collection;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description 测试专用
 * @since 2021-08-17 14:52
 */
@Slf4j
public class BizApplicationStarter implements InitializingBean {

    private EventManager eventManager;
    private IApiDiscovererService apiDiscovererService;
    private IServerManager serverManager;
    private IPf4jPluginManagerService pf4jPluginManagerService;
    private IApiRegisterService apiRegisterService;
    private IApiContextManagerService apiContextManagerService;

    @Autowired
    public BizApplicationStarter(EventManager eventManager, IApiDiscovererService apiDiscovererService, IServerManager serverManager, IPf4jPluginManagerService pf4jPluginManagerService, IApiRegisterService apiRegisterService, IApiContextManagerService apiContextManagerService) {
        this.eventManager = eventManager;
        this.apiDiscovererService = apiDiscovererService;
        this.serverManager = serverManager;
        this.pf4jPluginManagerService = pf4jPluginManagerService;
        this.apiRegisterService = apiRegisterService;
        this.apiContextManagerService = apiContextManagerService;
    }

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties and
     * satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     *
     * <p>This method allows the bean instance to perform validation of its overall configuration and
     * final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an essential
     *                   property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        // 创建http服务器,并开始处理请求
        Vertx.vertx().createHttpServer().requestHandler(req -> {
            // 路径匹配
            if ("/ws".equals(req.path())) {
                log.info("ws conn...");
                Future<ServerWebSocket> future = req.toWebSocket();

                future.onSuccess(new Handler<ServerWebSocket>() {
                    @Override
                    public void handle(ServerWebSocket webSocket) {
                        System.out.println("测试代码:建立长连接成功");

                        webSocket.textMessageHandler(msg -> {
                            if (StringUtils.equals("1", msg)) {

                                Api api = apiDiscovererService.get("/api/test2");

                                eventManager.publishEvent(ApiReactorEvent.UNDEPLOY, api);
                            } else if (StringUtils.equals("2", msg)) {

                                Api api = apiDiscovererService.get("/api/test2");

                                eventManager.publishEvent(ApiReactorEvent.DEPLOY, api);
                            } else if (StringUtils.equals("3", msg)) {

                                serverManager.stopServers().onSuccess(handler -> log.info("stop success"));
                            } else if (StringUtils.equals("4", msg)) {

                                serverManager.startServers();
                            } else if (StringUtils.equals("5", msg)) {

                                serverManager.stopServers()
                                        // 若停止服务成功 重新加载组件
                                        .onSuccess(handler -> {
                                            Pf4jPluginInfo pluginInfo = new Pf4jPluginInfo();
                                            pluginInfo.setPluginId("vertxServer");
                                            pluginInfo.setPluginPath("/Users/qiushi/ideaProject/qm/agw/agwPluginsAndLogsFolder/plugins/qm-agw-component-server-router-1.0.0-SNAPSHOT");
                                            pluginInfo.setType(Pf4jPluginTypeEnum.COMPONENT_PLUGIN);
                                        })
                                        // 若加载组件成功
                                        .onSuccess(event -> serverManager.startServers());
                            } else if (StringUtils.equals("6", msg)) {

                                Collection<AbstractApplicationContext> apiContexts = apiContextManagerService.getApplicationContexts();

                                apiContexts.stream().forEach(apiContext -> apiContext.close());

                                Pf4jPluginInfo pluginInfo = new Pf4jPluginInfo();
                                pluginInfo.setPluginId("httpProxy");
                                pluginInfo.setPluginPath("/Users/qiushi/ideaProject/qm/agw/agwPluginsAndLogsFolder/plugins/qm-agw-plugin-proxy-http-1.0.0-SNAPSHOT");
                                pluginInfo.setType(Pf4jPluginTypeEnum.COMPONENT_PLUGIN);
                                pf4jPluginManagerService.reloadApiPlugin(pluginInfo);

                                Collection<Api> apis = apiDiscovererService.apis();

                                apis.stream().forEach(api -> apiRegisterService.reRegister(api));
                            }
                        });

                        // 处理连接关闭
                        webSocket.closeHandler(handler -> {
                            log.info("ws close!");
                        });
                    }
                });

                future.onComplete(new Handler<AsyncResult<ServerWebSocket>>() {
                    @Override
                    public void handle(AsyncResult<ServerWebSocket> event) {
                        System.out.println("测试代码:onComplete");
                    }
                });

                future.onFailure(new Handler<Throwable>() {
                    @Override
                    public void handle(Throwable event) {
                        System.out.println("建立长连接失败");
                    }
                });
            } else {
                // 处理http请求
                req.response().putHeader("content-type", "text/plain").end("this is http request,visited:" + req.path());
            }
        }).listen(8888);
    }
}
