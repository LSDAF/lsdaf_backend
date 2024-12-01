package com.lsadf.core.cache;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;

public interface Cache<T> {

  /**
   * Get the value for the key
   *
   * @param key The key to get the value for
   * @return The value for the key if present
   */
  Optional<T> get(String key);

  /**
   * Set the value for the key
   *
   * @param key The key to set the value for
   * @param value The value to set
   */
  void set(String key, T value);

  /**
   * Set the value for the key with an expiration time
   *
   * @param key The key to set the value for
   * @param value The value to set
   * @param expirationSeconds The expiration time in seconds
   */
  void set(String key, T value, int expirationSeconds);

  /**
   * Unset the value for the key
   *
   * @param key The key to unset the value for
   */
  void unset(String key);

  /**
   * Get all the entries in the cache
   *
   * @return A map of all the entries in the cache
   */
  Map<String, T> getAll();

  /** Clear the cache */
  void clear();

  /**
   * Get the logger for the cache
   *
   * @return The logger for the cache
   */
  Logger getLogger();

  /**
   * Set the cache to be enabled or disabled
   *
   * @param enabled Whether the cache should be enabled or disabled
   */
  void setEnabled(boolean enabled);

  /**
   * Check if the cache is enabled
   *
   * @return Whether the cache is enabled
   */
  boolean isEnabled();

  /**
   * Set the expiration time for the cache
   *
   * @param expirationSeconds The expiration time in seconds
   */
  void setExpiration(int expirationSeconds);
}
