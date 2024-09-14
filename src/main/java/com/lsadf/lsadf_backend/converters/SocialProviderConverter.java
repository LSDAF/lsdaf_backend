package com.lsadf.lsadf_backend.converters;

import com.lsadf.lsadf_backend.constants.SocialProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SocialProviderConverter implements AttributeConverter<SocialProvider, String> {

    @Override
    public String convertToDatabaseColumn(SocialProvider socialProvider) {
        return socialProvider.getProviderType();
    }

    @Override
    public SocialProvider convertToEntityAttribute(String s) {
        return SocialProvider.fromString(s);
    }
}
