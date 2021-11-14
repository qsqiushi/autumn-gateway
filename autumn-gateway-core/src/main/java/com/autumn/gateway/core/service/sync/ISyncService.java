package com.autumn.gateway.core.service.sync;

import com.autumn.gateway.core.service.IService;

/**
 * <与管理端同步信息接口>
 *
 * @author qiushi
 * @since 2021/8/16 14:30
 */
public interface ISyncService extends IService, Runnable {
  /**
   * <开启同步>
   *
   * @param
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/16 14:34
   */
  void doStart();
  /**
   * <停止同步>
   *
   * @param
   * @return : void
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/16 14:35
   */
  void doStop();
  /**
   * <是否已经同步所有>
   *
   * @param
   * @return boolean
   * @author qiushi
   * @updator qiushi
   * @since 2021/8/16 14:35
   */
  boolean isAllSync();
}
