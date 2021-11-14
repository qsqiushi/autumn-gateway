package com.autumn.gateway.core.pojo.writer;

import com.autumn.gateway.core.enums.WriterPolicyTypeEnum;
import lombok.Data;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-09-06:11:03
 */
@Data
public class WriterPolicy {

  /** 类型 */
  private WriterPolicyTypeEnum type;

  /** 具体策略 */
  private String policy;
}
