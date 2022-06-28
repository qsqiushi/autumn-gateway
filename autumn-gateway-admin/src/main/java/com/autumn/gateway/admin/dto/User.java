package com.autumn.gateway.admin.dto;

import lombok.Data;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-06-23 19:36
 */
@Data
public class User {
  /** 姓名 */
  private String name;

  /** 年龄 */
  private String age;

  /** 电话 */
  private String mobile;
}
