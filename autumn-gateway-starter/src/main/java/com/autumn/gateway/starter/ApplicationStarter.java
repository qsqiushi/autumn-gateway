package com.autumn.gateway.starter;

import com.autumn.gateway.config.ApplicationConfiger;
import com.autumn.gateway.core.enums.SystemRunStateEnum;
import com.autumn.gateway.core.enums.SystemStartTypeEnum;
import com.autumn.gateway.core.enums.SystemStopTypeEnum;
import com.autumn.gateway.core.server.IServerManager;
import com.autumn.gateway.core.service.cluster.IVertxManagerService;
import com.autumn.gateway.core.service.server.IServer;
import com.autumn.gateway.core.service.sync.ISyncService;
import com.autumn.gateway.service.IPf4jPluginManagerService;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 系统启动器
 *
 * @author qius 2021/7/3
 * @updater qiushi
 */
@Slf4j
@Component
public class ApplicationStarter implements ApplicationRunner {

  @Resource private IPf4jPluginManagerService pluginManagerService;

  @Resource private ApplicationConfiger applicationConfiger;

  @Resource private IServerManager serverManager;
  /** 启动容器上下文 */
  @Resource ApplicationContext applicationContext;
  /** 网关当前运行状态 */
  private SystemRunStateEnum runState = SystemRunStateEnum.STOPPED;

  /**
   * SpringBoot 启动时运行
   *
   * @param args 运行参数
   */
  @Override
  public void run(ApplicationArguments args) {
    // 启动服务
    this.start(SystemStartTypeEnum.NORMAL);
  }

  /**
   * 启动网关服务
   *
   * @param startType 启动类型
   */
  public void start(SystemStartTypeEnum startType) {
    // 检查当前状态
    if (runState != SystemRunStateEnum.STOPPED) {
      log.info("current status of servers is [{}],can't start now", runState);
      return;
    }
    // 启动服务
    startServer(startType);
  }

  /**
   * 根据传入的配置参数启动网关服务
   *
   * @param startType 启动类型
   */
  public void startServer(SystemStartTypeEnum startType) {
    Future.future(
            event -> {
              log.debug("system starting with {} mode...", startType);
              this.setRunState(SystemRunStateEnum.STARTING);
              // 加载verticles 成功后启动服务
              loadVerticlesThenStartServers(startType);
              ISyncService syncService = applicationContext.getBean(ISyncService.class);
              // 异步启动同步线程
              Thread thread = new Thread(syncService);
              thread.start();

              event.complete();
            })
        .onSuccess(
            handler -> {
              this.setRunState(SystemRunStateEnum.RUNNING);
              log.info("system start finished with [{}] mode.", startType);
            })
        .onFailure(
            ex -> {
              log.info("some errors occurred during startup ", ex);
              runState = SystemRunStateEnum.ERROR;
            });
  }

  /**
   * <加载verticles 成功后启动服务>
   *
   * @param startType
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/11 15:31
   */
  private void loadVerticlesThenStartServers(SystemStartTypeEnum startType) {
    // 获得系统配置
    JsonObject jsonObject = applicationConfiger.getProperty("sysConfig", JsonObject.class);
    // 获得系统策略中 需要加载的verticles 类名称
    JsonArray jsonArray = jsonObject.getJsonArray("verticles");
    DeploymentOptions deploymentOptions =
        new DeploymentOptions()
            .setMaxWorkerExecuteTime(3000)
            .setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);
    // 实例化vertx
    Vertx vertx = applicationContext.getBean(IVertxManagerService.class).getVertx();
    // 部署verticles
    List<Future> futures =
        jsonArray.stream()
            .map(
                clzName ->
                    Future.<String>future(
                        r -> {
                          try {
                            vertx.deployVerticle(clzName.toString(), deploymentOptions, r);
                          } catch (Exception e) {
                            e.printStackTrace();
                          }
                        }))
            .collect(Collectors.toList());

    CompositeFuture.all(futures)
        .onSuccess(
            res -> {
              log.info("all verticles have bean deployed");

              startServers();

              this.setRunState(SystemRunStateEnum.RUNNING);

              log.debug("system start finished with {} mode.", startType);
            });
  }

  /**
   * 重启网关服务 若要重新加载配置 需要刷新系统配置
   *
   * @see ApplicationConfiger#refresh
   * @param startType 启动类型
   */
  public void restart(SystemStartTypeEnum startType) {
    SystemStopTypeEnum stopType = this.startStateToStopState(startType);
    this.stop(stopType).onSuccess(handler -> this.start(startType));
  }

  /** 停止网关服务 */
  public Future<Void> stop(SystemStopTypeEnum stopType) {
    log.debug("system stopping with {} mode...", stopType);
    this.setRunState(SystemRunStateEnum.STOPPING);

    // 停止服务，成功后更新状态
    return serverManager
        .stopServers()
        .onSuccess(
            handler -> {
              this.setRunState(SystemRunStateEnum.STOPPED);
              if (log.isDebugEnabled()) {
                log.debug("system stopped with [{}] mode...", stopType);
              }
            });
  }

  /**
   * 获取网关当前运行状态
   *
   * @return 网关当前运行状态
   */
  public SystemRunStateEnum getRunState() {
    return this.runState;
  }

  /**
   * 根据策略启动伺服服务
   *
   * @return 伺服服务实例
   */
  private Future<Collection<IServer>> startServers() {
    return serverManager.startServers();
  }

  /**
   * 更新网关当前运行状态
   *
   * @param state 网关当前运行状态
   */
  private void setRunState(SystemRunStateEnum state) {
    this.runState = state;
  }

  /**
   * 将启动类型转换为匹配的停止类型
   *
   * @param startType 启动类型
   * @return 匹配的停止类型
   */
  private SystemStopTypeEnum startStateToStopState(SystemStartTypeEnum startType) {
    switch (startType) {
      case RESTART:
        return SystemStopTypeEnum.RESTART;
      case RE_CONFIG:
        return SystemStopTypeEnum.RE_CONFIG;
      default:
        return SystemStopTypeEnum.NORMAL;
    }
  }
}
