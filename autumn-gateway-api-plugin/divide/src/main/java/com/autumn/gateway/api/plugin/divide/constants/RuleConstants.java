package com.autumn.gateway.api.plugin.divide.constants;

import com.netflix.loadbalancer.*;

/**
 * @author qiushi
 * @program agw
 * @description 规则常量
 * @since 2022-02-17 14:58
 */
public class RuleConstants {
  private RuleConstants() {}

  /**
   * 可用过滤策略 该类扩展了线性轮询策略，会先通过默认的线性轮询策略选取一个Provider，再去判断该Provider 是否超时可用， 当前连接数是否超过限制，如果都满足要求，则成功返回。
   *
   * <p>简单来说，AvailabilityFilteringRule将对候选的Provider进行可用性过滤，会先过滤掉多次访问
   * 故障而处于断路器跳闸状态的Provider服务，还会过滤掉并发的连接数超过阈值的Provider服务，然后，对剩余的服务列表进行线性轮询
   */
  public static final IRule AVAILABILITY_FILTERING_RULE = new AvailabilityFilteringRule();

  /** 随机 */
  public static final IRule RANDOM_RULE = new RandomRule();
  /**
   * 重试策略(RetryRule) // 该类会在一定的时限内进行Provider循环重试。RetryRule会在每次选取之后，对选举的Provider进行判断，如果为null或者not //
   * alive，会在一定的时限内(如500ms)内会不停的选取和判断。
   */
  public static final IRule RETRY_RULE = new RetryRule();
  /** 响应时间权重策略 */
  public static final IRule WEIGHTED_RESPONSE_TIME_RULE = new WeightedResponseTimeRule();
  /** 最少连接策略 */
  public static final IRule BEST_AVAILABLE_RULE = new BestAvailableRule();
  /** 线性轮训 */
  public static final IRule ROUND_ROBIN_RULE = new RoundRobinRule();
}
