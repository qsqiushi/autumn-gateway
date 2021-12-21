package com.autumn.gateway.component.policy.provider.util;

import com.autumn.gateway.api.plugin.core.api.pojo.PluginConfigInfo;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiushi
 * @program agw
 * @description
 * @since create 2021-08-16:08:43
 */
public class PolicyUtil {

  private PolicyUtil() {}

  /**
   * <解释插件配置字符串获得插件配置>
   *
   * @param plugins 插件信息字符串
   * @return : java.util.List<com.qm.agw.core.plugin.api.pojo.PluginConfigInfo>
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/16 08:46
   */
  public static List<PluginConfigInfo> explainPluginConfig(String plugins) {

    if (StringUtils.isNotEmpty(plugins)) {
      JsonArray pluginInfoJsonArray = new JsonArray(plugins);

      return pluginInfoJsonArray.stream()
          .map(
              item -> {
                JsonObject jsonObject = (JsonObject) item;
                return jsonObject.mapTo(PluginConfigInfo.class);
              })
          .collect(Collectors.toList());
    }

    return new ArrayList<>();
  }
}
