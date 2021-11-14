package com.autumn.gateway.config;

import com.autumn.gateway.core.configer.impl.BaseConfiger;
import com.autumn.gateway.core.constant.SystemConstant;
import com.google.common.io.CharStreams;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统配置
 *
 * @author qius 2021/7/3
 */
@Slf4j
@Configuration
public class ApplicationConfiger extends BaseConfiger {
  private static final String APP_CONFIG_FILE = "application.json";

  /** 构造方法 */
  public ApplicationConfiger() {
    // 从配置文件加载系统设置
    initFromConfig();
  }

  public ApplicationConfiger refresh(String str) {
    env = new JsonObject(str);
    return this;
  }

  /** 从配置文件加载系统设置 */
  private void initFromConfig() {
    try {
      InputStream inputStream = new ClassPathResource(APP_CONFIG_FILE).getInputStream();
      String str =
          CharStreams.toString(new InputStreamReader(inputStream, SystemConstant.CHARSET_NAME));
      env = new JsonObject(str);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public JsonObject getPluginProperty(String pluginId) {

    // 组件策略
    JsonArray jsonArray = getProperty("componentPolicy", JsonArray.class);

    List<JsonObject> list =
        jsonArray.stream()
            .filter(
                temp -> {
                  JsonObject jsonObject = (JsonObject) temp;
                  return jsonObject.getString("componentId").equals(pluginId);
                })
            .map(item -> (JsonObject) item)
            .collect(Collectors.toList());

    if (CollectionUtils.isEmpty(list)) {
      return null;
    }

    JsonObject param = list.get(0).getJsonObject("param");

    return param;
  }
}
