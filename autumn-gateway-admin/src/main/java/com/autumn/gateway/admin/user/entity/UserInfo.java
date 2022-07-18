package com.autumn.gateway.admin.user.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * UserInfo: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@FluentMybatis(
    table = "t_user_info",
    schema = "autumn_gateway",
    suffix = ""
)
public class UserInfo extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      auto = true
  )
  private Integer id;

  @Override
  public final Class entityClass() {
    return UserInfo.class;
  }
}
