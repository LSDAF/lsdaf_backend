package com.lsadf.lsadf_backend.constants;

import lombok.Getter;

@Getter
public enum SocialProvider {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    LOCAL("local");

    SocialProvider(String providerType) {
        this.providerType = providerType;
    }

    private final String providerType;

    public static SocialProvider getDefaultSocialProvider() {
        return LOCAL;
    }
}
