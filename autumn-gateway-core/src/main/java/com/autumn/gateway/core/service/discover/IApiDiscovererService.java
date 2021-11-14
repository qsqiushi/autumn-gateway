package com.autumn.gateway.core.service.discover;

import com.autumn.gateway.api.plugin.core.api.pojo.Api;
import com.autumn.gateway.core.service.IService;

import java.util.Collection;

/**
 * <>
 *
 * @author qiushi
 * @since 2021/6/18 15:01
 */
public interface IApiDiscovererService extends IService {

  /**
   * Returns a collection of deployed {@link Api}s.
   *
   * @return A collection of deployed {@link Api}s.
   */
  Collection<Api> apis();

  /**
   * Retrieve a deployed {@link Api} using its url.
   *
   * @param apiUrl The url of the deployed API.
   * @return A deployed {@link Api}
   */
  Api get(String apiUrl);
}
