package com.autumn.gateway.core.service.plugin;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.api.pojo.PluginConfigInfo;
import com.autumn.gateway.api.plugin.core.enums.RefreshStatusEnum;
import com.autumn.gateway.api.plugin.core.factory.IPluginFactory;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.core.service.policy.provider.IBaseBizPolicyProvider;
import com.autumn.gateway.core.service.policy.provider.IPluginPolicyProvider;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-08-16:19:08
 */
@Slf4j
public abstract class AbstractPluginManagerService {

  private RefreshStatusEnum refreshStatus = RefreshStatusEnum.NOT_REFRESH;

  private Map<String, List<IPlugin>> pluginMap = new HashMap<>();

  public List<IPlugin> getPluginList(String bizId) {

    switch (refreshStatus) {
      case NOT_REFRESH:
        refresh(bizId);
        break;
      case REFRESHING:
        while (true) {
          if (refreshStatus == RefreshStatusEnum.REFRESHED) {
            break;
          }
        }
        break;
      case REFRESHED:
        break;
    }

    return pluginMap.get(bizId) == null ? new ArrayList<>() : pluginMap.get(bizId);
  }
  /**
   * <获得策略提供者>
   *
   * @param
   * @return IBaseBizPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 18:19
   */
  public abstract IBaseBizPolicyProvider getPolicyProvider();
  /**
   * <获取插件策略提供者>
   *
   * @param
   * @return : IPluginPolicyProvider
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 18:25
   */
  public abstract IPluginPolicyProvider getPluginPolicyProvider();

  public synchronized void syncRefresh(String bizId) {
    refreshStatus = RefreshStatusEnum.REFRESHING;
    List<PluginConfigInfo> list = getPolicyProvider().getPluginsInfo(bizId);
    pluginMap.remove(bizId);
    pluginMap.put(bizId, instPlugin(list));
    refreshStatus = RefreshStatusEnum.REFRESHED;
  }

  public Future<Void> refresh(String bizId) {
    return Future.future(
        event -> {
          syncRefresh(bizId);
          event.complete();
        });
  }

  /**
   * <获取上下文>
   *
   * @param
   * @return : org.springframework.context.ApplicationContext
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/7 18:23
   */
  public abstract ApplicationContext getApplicationContext();

  /**
   * <根据插件配置添加插件实例>
   *
   * @param list
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/16 08:52
   */
  private List<IPlugin> instPlugin(List<PluginConfigInfo> list) {

    List<IPlugin> result = new ArrayList<>();

    // 判断非空
    if (!CollectionUtils.isEmpty(list)) {

      for (PluginConfigInfo pluginConfigInfo : list) {
        // 判断是否包含
        if (getApplicationContext().containsBean(pluginConfigInfo.getPluginId())) {

          IPluginFactory pluginFactory =
              getApplicationContext()
                  .getParent()
                  .getBeansOfType(IPluginFactory.class)
                  .get(pluginConfigInfo.getPluginId());

          // 根据策略ID找到插件策略
          String pluginPolicy =
              getApplicationContext()
                  .getBean(IPluginPolicyProvider.class)
                  .getPolicy(pluginConfigInfo.getPolicyId());
          // 构造插件参数
          PluginParam pluginParam = new PluginParam().setJsonParam(pluginPolicy);

          try {
            // 创建插件实例并添加到List
            result.add(pluginFactory.create(pluginParam));
          } catch (Exception ex) {

            log.error("API插件实例化异常", ex);
            continue;
          }
        }
      }
    }
    return result;
  }
}
