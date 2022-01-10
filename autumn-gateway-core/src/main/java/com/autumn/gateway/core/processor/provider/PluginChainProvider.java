package com.autumn.gateway.core.processor.provider;

import com.autumn.gateway.api.plugin.core.IPlugin;
import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.api.pojo.PluginConfigInfo;
import com.autumn.gateway.api.plugin.core.factory.IPluginFactory;
import com.autumn.gateway.api.plugin.core.pojo.PluginParam;
import com.autumn.gateway.core.plugin.DefaultLastPlugin;
import com.autumn.gateway.core.processor.chain.DefaultPluginChain;
import com.autumn.gateway.core.service.IApiHandlerProcessService;
import com.autumn.gateway.core.service.plugin.AbstractProductClassifyPluginManagerService;
import com.autumn.gateway.core.service.plugin.AbstractProductPluginManagerService;
import com.autumn.gateway.core.service.plugin.AbstractSysPluginManagerService;
import com.autumn.gateway.core.service.policy.provider.IPluginPolicyProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program autumn-gateway
 * @description 插件链提供器
 * @author qiushi
 * @since 2021-07-21 15:16
 */
public class PluginChainProvider {

  private List<IPlugin> apiPluginList = new ArrayList<>();

  /** 插件策略提供器 */
  @Resource private IPluginPolicyProvider pluginPolicyProvider;

  @Resource private AbstractSysPluginManagerService sysPluginManagerService;

  @Resource private AbstractProductPluginManagerService productPluginManagerService;

  @Resource private AbstractProductClassifyPluginManagerService productClassifyPluginManagerService;

  /** api上下文 */
  @Resource private ApplicationContext apiApplicationContext;

  @Resource private Api api;
  // TODO  没有响应处理插件而引入的临时代码
  @Resource private DefaultLastPlugin defaultLastPlugin;

  @Resource private IApiHandlerProcessService apiHandlerProcessService;

  public DefaultPluginChain create() {

    // 全链路插件
    List<IPlugin> allPluginList = new ArrayList<>();

    // 添加系统API插件
    allPluginList.addAll(sysPluginManagerService.getPluginList("sys"));

    // 添加产品API插件
    allPluginList.addAll(productPluginManagerService.getPluginList(api.getProductId()));

    // 添加产品分类API插件
    allPluginList.addAll(
        productClassifyPluginManagerService.getPluginList(api.getProductClassifyId()));

    // api插件
    if (CollectionUtils.isEmpty(apiPluginList)) {
      apiPluginList = getPlugin(api.getPluginInfos());
    }
    allPluginList.addAll(apiPluginList);
    // TODO  没有响应处理插件而引入的临时代码
    allPluginList.add(defaultLastPlugin);

    // TODO  之后看是否需要API参数 可以去掉
    DefaultPluginChain defaultPluginChain = new DefaultPluginChain(allPluginList, api);
    defaultPluginChain.afterHandler(ctx -> apiHandlerProcessService.processDecr(api));
    apiHandlerProcessService.processIncr(api);
    return defaultPluginChain;
  }
  /**
   * <根据插件配置添加插件实例>
   *
   * @param pluginConfigInfos
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/16 08:52
   */
  private List<IPlugin> getPlugin(List<PluginConfigInfo> pluginConfigInfos) {

    List<IPlugin> pluginList = new ArrayList<>();
    // 判断非空
    if (!CollectionUtils.isEmpty(pluginConfigInfos)) {

      for (PluginConfigInfo pluginConfigInfo : pluginConfigInfos) {
        // 判断是否包含
        if (apiApplicationContext.containsBean(pluginConfigInfo.getPluginId())) {

          if (apiApplicationContext.getParent() == null
              || apiApplicationContext.getParent().getParent() == null) {
            throw new RuntimeException("核心端组件异常");
          }

          IPluginFactory pluginFactory =
              apiApplicationContext
                  // bizContext
                  .getParent()
                  // startContext
                  .getParent()
                  .getBeansOfType(IPluginFactory.class)
                  .get(pluginConfigInfo.getPluginId());

          // 根据策略ID找到插件策略
          String pluginPolicy = pluginPolicyProvider.getPolicy(pluginConfigInfo.getPolicyId());
          // 构造插件参数
          PluginParam pluginParam = new PluginParam().setJsonParam(pluginPolicy);

          // 创建插件实例并添加到List
          pluginList.add(pluginFactory.create(pluginParam));
        }
      }
    }
    return pluginList;
  }
}
