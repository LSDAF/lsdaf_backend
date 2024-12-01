package com.lsadf.core.services.impl;

import com.lsadf.core.services.CacheFlushService;

public class NoOpFlushServiceImpl implements CacheFlushService {

  /** {@inheritDoc} */
  @Override
  public void flushCharacteristics() {
    // Do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void flushCurrencies() {
    // Do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void flushStages() {
    // Do nothing
  }
}
