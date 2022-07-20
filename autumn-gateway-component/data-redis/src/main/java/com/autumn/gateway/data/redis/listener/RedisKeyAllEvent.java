package com.autumn.gateway.data.redis.listener;

import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisKeyspaceEvent;
import org.springframework.data.redis.core.convert.MappingRedisConverter;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-19 15:46
 */
public class RedisKeyAllEvent<T> extends RedisKeyspaceEvent {

  /** Use {@literal UTF-8} as default charset. */
  private static final Charset CHARSET = StandardCharsets.UTF_8;

  private final MappingRedisConverter.BinaryKeyspaceIdentifier objectId;
  private final @Nullable Object value;

  /**
   * Creates new {@link RedisKeyAllEvent}.
   *
   * @param key the expired key.
   */
  public RedisKeyAllEvent(byte[] key) {
    this(key, null);
  }

  /**
   * Creates new {@link RedisKeyAllEvent}
   *
   * @param key the expired key.
   * @param value the value of the expired key. Can be {@literal null}.
   */
  public RedisKeyAllEvent(byte[] key, @Nullable Object value) {
    this(null, key, value);
  }

  /**
   * Creates new {@link RedisKeyExpiredEvent}
   *
   * @param channel the Pub/Sub channel through which this event was received.
   * @param key the expired key.
   * @param value the value of the expired key. Can be {@literal null}.
   * @since 1.8
   */
  public RedisKeyAllEvent(@Nullable String channel, byte[] key, @Nullable Object value) {
    super(channel, key);

    if (MappingRedisConverter.BinaryKeyspaceIdentifier.isValid(key)) {
      this.objectId = MappingRedisConverter.BinaryKeyspaceIdentifier.of(key);
    } else {
      this.objectId = null;
    }

    this.value = value;
  }

  /**
   * Gets the keyspace in which the expiration occured.
   *
   * @return {@literal null} if it could not be determined.
   */
  public String getKeyspace() {
    return objectId != null ? new String(objectId.getKeyspace(), CHARSET) : null;
  }

  /**
   * Get the expired objects id.
   *
   * @return the expired objects id.
   */
  public byte[] getId() {
    return objectId != null ? objectId.getId() : getSource();
  }

  /**
   * Get the expired Object
   *
   * @return {@literal null} if not present.
   */
  @Nullable
  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {

    byte[] id = getId();
    return "RedisKeyAllEvent [keyspace="
        + getKeyspace()
        + ", id="
        + (id == null ? null : new String(id))
        + "]";
  }
}
