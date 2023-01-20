package com.autumn.gateway.component.api.discover.service.impl;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.api.plugin.core.api.pojo.PluginConfigInfo;
import com.autumn.gateway.component.api.discover.enums.RedisKeyEnums;
import com.autumn.gateway.core.service.discover.IApiDiscovererService;
import com.autumn.gateway.data.redis.service.RedisService;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @program  autumn
 * @description
 * @author qiushi
 * @since 2021-07-26 10:25
 */
@Slf4j
@Service
public class RedisApiDiscovererServiceImpl implements IApiDiscovererService {

  @Resource private RedisService<String> redisService;

  /**
   * Returns a collection of deployed {@link Api}s.
   *
   * @return A collection of deployed {@link Api}s.
   */
  @Override
  public Collection<Api> apis() {

    List<Api> apiList = new ArrayList<>();

    String prefix = RedisKeyEnums.GATEWAY_API_URL.getPrefix().concat("*");
    // 找到redisAPI 所有的key
    Set<String> apiKeys = redisService.scan(prefix);
    for (String key : apiKeys) {
      // 找到对应的hash
      Map<String, String> objectMap = redisService.hmget(key);
      // 处理map为Api实例
      Api api = explainMap2Api(objectMap);
      apiList.add(api);
    }
    return apiList;
  }

  private Api explainMap2Api(Map<String, String> objectMap) {
    Api api = new Api();
    // 赋值给对象
    try {
      BeanUtils.populate(api, objectMap);
    } catch (Exception e) {
      log.error("Failed to populate", e);
      return null;
    }
    String plugin = api.getPlugin();
    JsonArray jsonArray = new JsonArray(plugin);

    // 解析API插件信息
    List<PluginConfigInfo> list =
        jsonArray.stream()
            .map(
                item -> {
                  JsonObject jsonObject = (JsonObject) item;
                  return jsonObject.mapTo(PluginConfigInfo.class);
                })
            .collect(Collectors.toList());

    api.setPluginInfos(list);
    return api;
  }

  @Override
  public Api get(String apiUrl) {

    String apiKey = String.format(RedisKeyEnums.GATEWAY_API_URL.getKeyPattern(), apiUrl);
    // 找到对应的hash
    Map<String, String> objectMap = redisService.hmget(apiKey);
    if (objectMap == null) {
      log.error("ApiDiscoverer can't find  api which url equals [{}]", apiUrl);
      return null;
    }

    return explainMap2Api(objectMap);
  }
}
