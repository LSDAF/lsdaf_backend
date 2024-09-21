package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.CurrencyService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final GameSaveService gameSaveService;
    private final Cache<Currency> currencyCache;
    private final Mapper mapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               Cache<Currency> currencyCache,
                               GameSaveService gameSaveService,
                               Mapper mapper) {
        this.currencyRepository = currencyRepository;
        this.currencyCache = currencyCache;
        this.gameSaveService = gameSaveService;

        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Currency getCurrency(String gameSaveId) throws NotFoundException {
        if (currencyCache.isEnabled()) {
            Optional<Currency> optionalCachedCurrency = currencyCache.get(gameSaveId);
            if (optionalCachedCurrency.isPresent()) {
                Currency currency = optionalCachedCurrency.get();
                if (currency.getAmethyst() == null || currency.getDiamond() == null || currency.getEmerald() == null || currency.getGold() == null) {
                    CurrencyEntity currencyEntity = getCurrencyEntity(gameSaveId);
                    if (currency.getAmethyst() == null) {
                        currency.setAmethyst(currencyEntity.getAmethystAmount());
                    }
                    if (currency.getDiamond() == null) {
                        currency.setDiamond(currencyEntity.getDiamondAmount());
                    }
                    if (currency.getEmerald() == null) {
                        currency.setEmerald(currencyEntity.getEmeraldAmount());
                    }
                    if (currency.getGold() == null) {
                        currency.setGold(currencyEntity.getGoldAmount());
                    }

                    return currency;
                }
                return currency;
            }
        }
        CurrencyEntity currencyEntity = getCurrencyEntity(gameSaveId);

        return mapper.mapCurrencyEntityToCurrency(currencyEntity);
    }

    @Override
    @Transactional
    public void saveCurrency(String gameSaveId, Currency currency, boolean toCache) throws NotFoundException {
        if (isCurrencyNull(currency)) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if (toCache) {
            currencyCache.set(gameSaveId, currency);
        } else {
            saveCurrencyToDatabase(gameSaveId, currency);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public CurrencyEntity getCurrencyEntity(String gameSaveId) throws NotFoundException {
        return currencyRepository.findById(gameSaveId)
                .orElseThrow(() -> new NotFoundException("Currency entity not found for game save id " + gameSaveId));
    }

    /**
     * Save the gold amount to the database
     * @param gameSaveId the id of the game save
     * @param currency the currency POJO
     * @throws NotFoundException if the currency entity is not found
     */
    @Transactional
    public void saveCurrencyToDatabase(String gameSaveId, Currency currency) throws NotFoundException {
        CurrencyEntity currencyEntity = getCurrencyEntity(gameSaveId);

        boolean hasUpdates = false;

        if (currency.getGold() != null) {
            currencyEntity.setGoldAmount(currency.getGold());
            hasUpdates = true;
        }
        if (currency.getDiamond() != null) {
            currencyEntity.setDiamondAmount(currency.getDiamond());
            hasUpdates = true;
        }
        if (currency.getEmerald() != null) {
            currencyEntity.setEmeraldAmount(currency.getEmerald());
            hasUpdates = true;
        }
        if (currency.getAmethyst() != null) {
            currencyEntity.setAmethystAmount(currency.getAmethyst());
            hasUpdates = true;
        }

        if (hasUpdates) {
            currencyRepository.save(currencyEntity);
        }
    }

    private static boolean isCurrencyNull(Currency currency) {
        return currency.getAmethyst() == null || currency.getDiamond() == null || currency.getEmerald() == null || currency.getGold() == null;
    }
}
