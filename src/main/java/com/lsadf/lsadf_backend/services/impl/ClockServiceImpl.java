package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.utils.DateUtils;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/**
 * Implementation of ClockService
 */
public class ClockServiceImpl implements ClockService {

    private Clock clock;

    public ClockServiceImpl(Clock clock) {
        this.clock = clock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clock getClock() {
        return this.clock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant nowInstant() {
        return Instant.now(clock);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date nowDate() {
        return DateUtils.dateFromClock(clock);
    }
}
