package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;

public interface CurrencyService {

    /**
     * Get the currencies of a game save
     * @param gameSaveId the id of the game save
     * @return the currency POJO
     * @throws NotFoundException if the currency entity is not found
     */
    Currency getCurrency(String gameSaveId) throws NotFoundException;

    /**
     * Save the currencies of a game save
     * @param gameSaveId the id of the game save
     * @param currency the currency POJO
     * @param toCache true if the currency should be saved to cache, false otherwise
     * @throws NotFoundException
     */
    void saveCurrency(String gameSaveId, Currency currency, boolean toCache) throws NotFoundException;

}