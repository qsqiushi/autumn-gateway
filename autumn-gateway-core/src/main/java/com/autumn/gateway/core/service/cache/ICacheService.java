package com.autumn.gateway.core.service.cache;

import com.autumn.gateway.core.pojo.AppInfo;
import com.autumn.gateway.core.pojo.cache.ProductClassifyInfo;
import com.autumn.gateway.core.pojo.cache.ProductInfo;
import com.autumn.gateway.core.pojo.cache.SysInfo;
import com.autumn.gateway.core.service.IService;

/**
 * @program autumn-gateway
 * @description 缓存
 * @author qiushi
 * @since 2021-08-12:14:10
 */
public interface ICacheService extends IService {
  /**
   * <获取系统信息>
   *
   * @param
   * @return : SysInfo
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 08:50
   */
  SysInfo getSysInfo();

  /**
   * <获得产品信息>
   *
   * @param productId
   * @return : ProductInfo
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 08:51
   */
  ProductInfo getProductInfo(String productId);
  /**
   * <获取产品分类信息>
   *
   * @param productClassifyId
   * @return : ProductClassifyInfo
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 09:30
   */
  ProductClassifyInfo getProductClassifyInfo(String productClassifyId);

  /**
   * <获取APP信息>
   *
   * @param appId
   * @return : AppInfo
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/30 09:32
   */
  AppInfo getAppInfo(String appId);
}
