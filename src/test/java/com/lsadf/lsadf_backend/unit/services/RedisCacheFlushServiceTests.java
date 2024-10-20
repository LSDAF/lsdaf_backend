package com.lsadf.lsadf_backend.unit.services;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.StageService;
import com.lsadf.lsadf_backend.services.impl.RedisCacheFlushServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;

class RedisCacheFlushServiceTests {

    RedisCacheFlushServiceImpl redisCacheFlushService;

    @Mock
    private Cache<Currency> currencyCache;

    @Mock
    private Cache<Stage> stageCache;

    @Mock
    CurrencyService currencyService;

    @Mock
    StageService stageService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.redisCacheFlushService = new RedisCacheFlushServiceImpl(currencyService, stageService, currencyCache, stageCache);
    }

    @Test
    void should_flush_currencies() {
        Map<String, Currency> currencyEntries = Map.of(
                "1", new Currency(1L, 2L, null, 3L),
                "2", new Currency(2L, 4L, null, 8L));
        when(currencyCache.getAll()).thenReturn(currencyEntries);

        redisCacheFlushService.flushCurrencies();
        verify(currencyService, times(1)).saveCurrency("1", new Currency(1L, 2L, null, 3L), false);
        verify(currencyService, times(1)).saveCurrency("2", new Currency(2L, 4L, null, 8L), false);
    }

    @Test
    void should_flush_stages() {
        Map<String, Stage> stageEntries = Map.of(
                "1", new Stage(10L, 20L),
                "2", new Stage(30L, 40L));
        when(stageCache.getAll()).thenReturn(stageEntries);

        redisCacheFlushService.flushStages();
        verify(stageService, times(1)).saveStage("1", new Stage(10L, 20L), false);
        verify(stageService, times(1)).saveStage("2", new Stage(30L, 40L), false);
    }

    @Test
    void do_nothing_when_flushing_empty_currency_cache() {
        Map<String, Currency> currencyEntries = Collections.emptyMap();
        when(currencyCache.getAll()).thenReturn(currencyEntries);

        redisCacheFlushService.flushCurrencies();
        verifyNoInteractions(currencyService);
    }

    @Test
    void do_nothing_when_flushing_empty_stage_cache() {
        Map<String, Stage> stageEntries = Collections.emptyMap();
        when(stageCache.getAll()).thenReturn(stageEntries);

        redisCacheFlushService.flushStages();
        verifyNoInteractions(stageService);
    }
}
