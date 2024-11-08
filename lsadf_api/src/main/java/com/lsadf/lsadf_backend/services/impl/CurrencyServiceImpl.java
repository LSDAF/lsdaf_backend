package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.services.CurrencyService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final Cache<Currency> currencyCache;
    private final Mapper mapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               Cache<Currency> currencyCache,
                               Mapper mapper) {
        this.currencyRepository = currencyRepository;
        this.currencyCache = currencyCache;

        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Currency getCurrency(String gameSaveId) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }
        if (currencyCache.isEnabled()) {
            Optional<Currency> optionalCachedCurrency = currencyCache.get(gameSaveId);
            if (optionalCachedCurrency.isPresent()) {
                Currency currency = optionalCachedCurrency.get();
                if (currency.getAmethyst() == null || currency.getDiamond() == null || currency.getEmerald() == null || currency.getGold() == null) {
                    CurrencyEntity currencyEntity = getCurrencyEntity(gameSaveId);
                    return mergeCurrencies(currency, currencyEntity);
                }
                return currency;
            }
        }
        CurrencyEntity currencyEntity = getCurrencyEntity(gameSaveId);

        return mapper.mapCurrencyEntityToCurrency(currencyEntity);
    }

    /**
     * Merge the currency POJO with the currency entity from the database
     * @param currency the currency POJO
     * @param currencyEntity the currency entity from the database
     * @return the merged currency POJO
     */
    private static Currency mergeCurrencies(Currency currency, CurrencyEntity currencyEntity) {
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

    @Override
    @Transactional
    public void saveCurrency(String gameSaveId, Currency currency, boolean toCache) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }
        if (currency == null || isCurrencyNull(currency)) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if (toCache) {
            currencyCache.set(gameSaveId, currency);
        } else {
            saveCurrencyToDatabase(gameSaveId, currency);
        }
    }

    /**
     * Get the currency entity from the database
     * @param gameSaveId the id of the game save
     * @return the currency entity
     * @throws NotFoundException if the currency entity is not found
     */
    private CurrencyEntity getCurrencyEntity(String gameSaveId) throws NotFoundException {
        return currencyRepository.findById(gameSaveId)
                .orElseThrow(() -> new NotFoundException("Currency entity not found for game save id " + gameSaveId));
    }

    /**
     * Save the gold amount to the database
     * @param gameSaveId the id of the game save
     * @param currency the currency POJO
     * @throws NotFoundException if the currency entity is not found
     */
    private void saveCurrencyToDatabase(String gameSaveId, Currency currency) throws NotFoundException {
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

    /**
     * Check if the currency is all null
     * @param currency the currency POJO
     * @return true if all fields are null, false otherwise
     */
    private static boolean isCurrencyNull(Currency currency) {
        return currency.getAmethyst() == null
                && currency.getDiamond() == null
                && currency.getEmerald() == null
                && currency.getGold() == null;
    }
}