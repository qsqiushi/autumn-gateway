package com.autumn.gateway.api.plugin.core.api.pojo;

import io.vertx.ext.web.FileUpload;
import lombok.Data;

/**
 * @author qiushi
 * @program agw
 * @description 文件上传
 * @since 2022-02-22 15:05
 */
@Data
public class AgwFileUpload {

  private FileUpload fileUpload;

  private String name;

  public AgwFileUpload(FileUpload fileUpload) {
    this.fileUpload = fileUpload;
    this.name = fileUpload.name();
  }
}
