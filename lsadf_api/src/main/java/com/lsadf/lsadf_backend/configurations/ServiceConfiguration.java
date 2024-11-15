package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.http_clients.KeycloakAdminClient;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.impl.MapperImpl;
import com.lsadf.core.models.Characteristics;
import com.lsadf.core.models.Currency;
import com.lsadf.core.models.Stage;
import com.lsadf.lsadf_backend.properties.KeycloakProperties;
import com.lsadf.lsadf_backend.repositories.*;
import com.lsadf.lsadf_backend.services.*;
import com.lsadf.lsadf_backend.services.impl.*;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

import static com.lsadf.core.constants.BeanConstants.Cache.GAME_SAVE_OWNERSHIP_CACHE;


/**
 * Configuration class for the services.
 */
@Configuration
public class ServiceConfiguration {

    @Bean
    public CharacteristicsService characteristicsService(CharacteristicsRepository characteristicsRepository,
                                                         Cache<Characteristics> characteristicsCache,
                                                         Mapper mapper) {
        return new CharacteristicsServiceImpl(characteristicsRepository, characteristicsCache, mapper);
    }

    @Bean
    public CurrencyService currencyService(CurrencyRepository currencyRepository,
                                           Cache<Currency> currencyCache,
                                           Mapper mapper) {
        return new CurrencyServiceImpl(currencyRepository, currencyCache, mapper);
    }

    @Bean
    public GameSaveService gameSaveService(UserService userService,
                                           GameSaveRepository gameSaveRepository,
                                           InventoryRepository inventoryRepository,
                                           StageRepository stageRepository,
                                           CharacteristicsRepository characteristicsRepository,
                                           CurrencyRepository currencyRepository,
                                           @Qualifier(GAME_SAVE_OWNERSHIP_CACHE) Cache<String> gameSaveOwnershipCache,
                                           HistoCache<Stage> stageHistoCache,
                                           HistoCache<Characteristics> characteristicsHistoCache,
                                           HistoCache<Currency> currencyHistoCache) {
        return new GameSaveServiceImpl(userService,
                gameSaveRepository,
                inventoryRepository,
                stageRepository,
                characteristicsRepository,
                currencyRepository,
                gameSaveOwnershipCache,
                stageHistoCache,
                characteristicsHistoCache,
                currencyHistoCache);
    }

    @Bean
    public InventoryService inventoryService(InventoryRepository inventoryRepository,
                                             ItemRepository itemRepository,
                                             Mapper mapper) {
        return new InventoryServiceImpl(inventoryRepository, itemRepository, mapper);
    }

    @Bean
    public StageService stageService(StageRepository stageRepository,
                                     Cache<Stage> stageCache,
                                     Mapper mapper) {
        return new StageServiceImpl(stageRepository, stageCache, mapper);
    }

    @Bean
    public SearchService searchService(UserService userService,
                                       GameSaveService gameSaveService) {
        return new SearchServiceImpl(userService, gameSaveService);
    }

    @Bean
    public UserService userService(Keycloak keycloak,
                                   KeycloakProperties keycloakProperties,
                                   KeycloakAdminClient keycloakAdminClient,
                                   ClockService clockService,
                                   Mapper mapper) {
        return new UserServiceImpl(keycloak,
                keycloakProperties,
                keycloakAdminClient,
                clockService,
                mapper);
    }

    @Bean
    public Mapper mapper() {
        return new MapperImpl();
    }

    @Bean
    public ClockService clockService(Clock clock) {
        return new ClockServiceImpl(clock);
    }

//    @Bean
//    public EmailService emailService(VelocityEngine velocityEngine,
//                                     JavaMailSender emailSender,
//                                     EmailProperties mailProperties,
//                                     ServerProperties serverProperties) {
//        return new EmailServiceImpl(velocityEngine, emailSender, mailProperties, serverProperties);
//    }

}
