package com.lsadf.lsadf_backend.requests.search;

public interface SearchFilter {
    String getFilerType();

    static GameSaveFilter fromString(String filterType) {
        for (GameSaveFilter f : GameSaveFilter.values()) {
            if (f.getFilerType().equalsIgnoreCase(filterType)) {
                return f;
            }
        }
        return null;
    }
}
