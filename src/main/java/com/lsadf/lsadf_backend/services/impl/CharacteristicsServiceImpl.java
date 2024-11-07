package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Characteristics;
import com.lsadf.lsadf_backend.services.CharacteristicsService;

public class CharacteristicsServiceImpl implements CharacteristicsService {
    private final CharacteristicsRepository characteristicsRepository;
    private final Cache<Characteristics> characteristicsCache;
    private final Mapper mapper;

    public CharacteristicsServiceImpl(CharacteristicsRepository characteristicsRepository,
                                      Cache<Characteristics> characteristicsCache,
                                      Mapper mapper) {
        this.characteristicsRepository = characteristicsRepository;
        this.characteristicsCache = characteristicsCache;
        this.mapper = mapper;
    }
}
