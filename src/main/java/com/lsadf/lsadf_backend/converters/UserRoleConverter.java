package com.lsadf.lsadf_backend.converters;

import com.lsadf.lsadf_backend.constants.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;

@Converter
public class UserRoleConverter implements AttributeConverter<Set<UserRole>, String> {
    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<UserRole> userRoleList) {
        if (userRoleList == null || userRoleList.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<UserRole> it = userRoleList.iterator();
        while (it.hasNext()) {
            sb.append(it.next().getRole());
            if (it.hasNext()) {
                sb.append(DELIMITER);
            }
        }

        return sb.toString();
    }

    @Override
    public Set<UserRole> convertToEntityAttribute(String string) {
        if (string == null || string.isEmpty()) {
            return emptySet();
        }

        List<String> roles = asList(string.split(DELIMITER));

        return roles.stream()
                .map(UserRole::fromRole)
                .collect(Collectors.toSet());
    }
}
