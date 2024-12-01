package com.lsadf.core.services.impl;

import com.lsadf.core.services.CacheService;

public class NoOpCacheServiceImpl implements CacheService {

  /** {@inheritDoc} */
  @Override
  public Boolean isEnabled() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public void toggleCacheEnabling() {
    // Do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void clearCaches() {
    // Do nothing
  }
}
