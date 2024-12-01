package com.lsadf.core.services;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

public interface ClockService {
  /**
   * Get the clock
   *
   * @return the clock
   */
  Clock getClock();

  /**
   * Set the clock
   *
   * @param clock the clock
   */
  void setClock(Clock clock);

  /**
   * Get the current instant
   *
   * @return the current instant
   */
  Instant nowInstant();

  /**
   * Get the current date
   *
   * @return the current date
   */
  Date nowDate();
}
