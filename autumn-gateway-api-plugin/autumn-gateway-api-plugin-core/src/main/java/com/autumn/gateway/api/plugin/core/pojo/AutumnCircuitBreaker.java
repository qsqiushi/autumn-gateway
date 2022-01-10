package com.autumn.gateway.api.plugin.core.pojo;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerState;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since create 2021-09-13 13:37
 */
@Data
public class AutumnCircuitBreaker implements CircuitBreaker, Comparable<AutumnCircuitBreaker> {

  private CircuitBreaker circuitBreaker;

  private Integer order;

  private Class clz;

  public AutumnCircuitBreaker(CircuitBreaker circuitBreaker, Integer order, Class clz) {

    this.circuitBreaker = circuitBreaker;

    this.order = order;

    this.clz = clz;
  }

  @Override
  public CircuitBreaker close() {
    return circuitBreaker.close();
  }

  @Override
  public CircuitBreaker openHandler(Handler<Void> handler) {
    return circuitBreaker.openHandler(handler);
  }

  @Override
  public CircuitBreaker halfOpenHandler(Handler<Void> handler) {
    return circuitBreaker.halfOpenHandler(handler);
  }

  @Override
  public CircuitBreaker closeHandler(Handler<Void> handler) {
    return circuitBreaker.closeHandler(handler);
  }

  @Override
  public <T> Future<T> executeWithFallback(
      Handler<Promise<T>> handler, Function<Throwable, T> function) {
    return circuitBreaker.executeWithFallback(handler, function);
  }

  @Override
  public <T> Future<T> execute(Handler<Promise<T>> handler) {
    return circuitBreaker.execute(handler);
  }

  @Override
  public <T> CircuitBreaker executeAndReport(Promise<T> promise, Handler<Promise<T>> handler) {
    return circuitBreaker.executeAndReport(promise, handler);
  }

  @Override
  public <T> CircuitBreaker executeAndReportWithFallback(
      Promise<T> promise, Handler<Promise<T>> handler, Function<Throwable, T> function) {
    return circuitBreaker.executeAndReportWithFallback(promise, handler, function);
  }

  @Override
  public <T> CircuitBreaker fallback(Function<Throwable, T> function) {
    return circuitBreaker.fallback(function);
  }

  @Override
  public CircuitBreaker reset() {
    return circuitBreaker.reset();
  }

  @Override
  public CircuitBreaker open() {
    return circuitBreaker.open();
  }

  @Override
  public CircuitBreakerState state() {
    return circuitBreaker.state();
  }

  @Override
  public long failureCount() {
    return circuitBreaker.failureCount();
  }

  @Override
  public String name() {
    return circuitBreaker.name();
  }

  @Override
  public CircuitBreaker retryPolicy(Function<Integer, Long> function) {
    return circuitBreaker.retryPolicy(function);
  }

  /**
   * Compares this object with the specified object for order. Returns a negative integer, zero, or
   * a positive integer as this object is less than, equal to, or greater than the specified object.
   *
   * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</tt> for all
   * <tt>x</tt> and <tt>y</tt>. (This implies that <tt>x.compareTo(y)</tt> must throw an exception
   * iff <tt>y.compareTo(x)</tt> throws an exception.)
   *
   * <p>The implementor must also ensure that the relation is transitive: <tt>(x.compareTo(y)&gt;0
   * &amp;&amp; y.compareTo(z)&gt;0)</tt> implies <tt>x.compareTo(z)&gt;0</tt>.
   *
   * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt> implies that
   * <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for all <tt>z</tt>.
   *
   * <p>It is strongly recommended, but <i>not</i> strictly required that <tt>(x.compareTo(y)==0) ==
   * (x.equals(y))</tt>. Generally speaking, any class that implements the <tt>Comparable</tt>
   * interface and violates this condition should clearly indicate this fact. The recommended
   * language is "Note: this class has a natural ordering that is inconsistent with equals."
   *
   * <p>In the foregoing description, the notation <tt>sgn(</tt><i>expression</i><tt>)</tt>
   * designates the mathematical <i>signum</i> function, which is defined to return one of
   * <tt>-1</tt>, <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i> is
   * negative, zero or positive.
   *
   * @param autumnCircuitBreaker the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException if the specified object's type prevents it from being compared to
   *     this object.
   */
  @Override
  public int compareTo(AutumnCircuitBreaker autumnCircuitBreaker) {
    return this.getOrder().compareTo(autumnCircuitBreaker.getOrder());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutumnCircuitBreaker that = (AutumnCircuitBreaker) o;
    return StringUtils.equals(
        this.circuitBreaker.name(), ((AutumnCircuitBreaker) o).getCircuitBreaker().name());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.circuitBreaker.name());
  }
}
