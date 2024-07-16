package com.lsadf.lsadf_backend.requests.search;

public enum GameSaveFilter implements SearchFilter {
    USER_ID("userId"),
    ID("id");

    private final String filterType;

    GameSaveFilter(String filterType) {
        this.filterType = filterType;
    }

    @Override
    public String getFilerType() {
        return filterType;
    }
}
