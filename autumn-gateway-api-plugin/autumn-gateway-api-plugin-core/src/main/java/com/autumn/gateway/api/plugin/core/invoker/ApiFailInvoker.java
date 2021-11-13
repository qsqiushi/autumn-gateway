package com.autumn.gateway.api.plugin.core.invoker;

import com.autumn.gateway.api.plugin.core.pojo.AutumnCircuitBreaker;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author qiushi
 * @program  autumn-gateway
 * @description
 * @since create 2021-09-13:13:35
 */
public class ApiFailInvoker {

  private List<AutumnCircuitBreaker> circuitBreakers;

  public ApiFailInvoker() {
    circuitBreakers = new ArrayList<>();
  }

  public List<AutumnCircuitBreaker> getCircuitBreakers() {
    return circuitBreakers;
  }

  /**
   * <添加熔断器 order越小的优先级越高>
   *
   * @param autumnCircuitBreaker 熔断器
   * @return java.util.List
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/13 13:52
   */
  public List<AutumnCircuitBreaker> add(AutumnCircuitBreaker autumnCircuitBreaker) {

    Iterator<AutumnCircuitBreaker> iterator = circuitBreakers.iterator();

    // 是否需要添加到list
    Boolean bl = Boolean.TRUE;

    // 判断是否重复
    while (iterator.hasNext()) {
      AutumnCircuitBreaker temp = iterator.next();

      if (StringUtils.equals(temp.name(), autumnCircuitBreaker.name())) {
        // 若同名且不相等则移除
        if (temp != autumnCircuitBreaker.getCircuitBreaker()) {
          iterator.remove();
        } else {
          bl = Boolean.FALSE;
        }
      }
    }
    if (bl) {
      circuitBreakers.add(autumnCircuitBreaker);
      Collections.sort(circuitBreakers);
    }
    return circuitBreakers;
  }

  public List<AutumnCircuitBreaker> remove(AutumnCircuitBreaker autumnCircuitBreaker) {
    circuitBreakers.remove(autumnCircuitBreaker);
    Collections.sort(circuitBreakers);
    return circuitBreakers;
  }
  /**
   * <获取有效的熔断器 order越小的优先级越高>
   *
   * @return
   * @author qiushi
   * @updator qiushi
   * @since 2021/9/13 13:55
   */
  public AutumnCircuitBreaker getEffective() {

    if (CollectionUtils.isEmpty(circuitBreakers)) {
      return null;
    } else {
      return circuitBreakers.get(0);
    }
  }
}
