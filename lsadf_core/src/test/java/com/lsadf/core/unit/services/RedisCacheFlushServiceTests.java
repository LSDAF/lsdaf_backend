package com.lsadf.core.unit.services;

import static org.mockito.Mockito.*;

import com.lsadf.core.cache.Cache;
import com.lsadf.core.models.Characteristics;
import com.lsadf.core.models.Currency;
import com.lsadf.core.models.Stage;
import com.lsadf.core.services.CharacteristicsService;
import com.lsadf.core.services.CurrencyService;
import com.lsadf.core.services.InventoryService;
import com.lsadf.core.services.StageService;
import com.lsadf.core.services.impl.RedisCacheFlushServiceImpl;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestMethodOrder(MethodOrderer.MethodName.class)
class RedisCacheFlushServiceTests {

  RedisCacheFlushServiceImpl redisCacheFlushService;

  @Mock private Cache<Characteristics> characteristicsCache;

  @Mock private Cache<Currency> currencyCache;

  @Mock private Cache<Stage> stageCache;

  @Mock private CharacteristicsService characteristicsService;

  @Mock CurrencyService currencyService;

  @Mock InventoryService inventoryService;

  @Mock StageService stageService;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    this.redisCacheFlushService =
        new RedisCacheFlushServiceImpl(
            characteristicsService,
            currencyService,
            inventoryService,
            stageService,
            characteristicsCache,
            currencyCache,
            stageCache);
  }

  @Test
  void should_flush_characteristics() {
    Map<String, Characteristics> characteristicsEntries =
        Map.of(
            "1", new Characteristics(1L, null, null, null, null),
            "2", new Characteristics(2L, null, null, null, null));
    when(characteristicsCache.getAll()).thenReturn(characteristicsEntries);

    redisCacheFlushService.flushCharacteristics();
    verify(characteristicsService, times(1))
        .saveCharacteristics("1", new Characteristics(1L, null, null, null, null), false);
    verify(characteristicsService, times(1))
        .saveCharacteristics("2", new Characteristics(2L, null, null, null, null), false);
  }

  @Test
  void should_flush_currencies() {
    Map<String, Currency> currencyEntries =
        Map.of(
            "1", new Currency(1L, 2L, null, 3L),
            "2", new Currency(2L, 4L, null, 8L));
    when(currencyCache.getAll()).thenReturn(currencyEntries);

    redisCacheFlushService.flushCurrencies();
    verify(currencyService, times(1)).saveCurrency("1", new Currency(1L, 2L, null, 3L), false);
    verify(currencyService, times(1)).saveCurrency("2", new Currency(2L, 4L, null, 8L), false);
  }

  @Test
  void should_flush_stages() {
    Map<String, Stage> stageEntries =
        Map.of(
            "1", new Stage(10L, 20L),
            "2", new Stage(30L, 40L));
    when(stageCache.getAll()).thenReturn(stageEntries);

    redisCacheFlushService.flushStages();
    verify(stageService, times(1)).saveStage("1", new Stage(10L, 20L), false);
    verify(stageService, times(1)).saveStage("2", new Stage(30L, 40L), false);
  }

  @Test
  void do_nothing_when_flushing_empty_characteristics_cache() {
    Map<String, Characteristics> characteristicsEntries = Collections.emptyMap();
    when(characteristicsCache.getAll()).thenReturn(characteristicsEntries);

    redisCacheFlushService.flushCharacteristics();
    verifyNoInteractions(characteristicsService);
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
