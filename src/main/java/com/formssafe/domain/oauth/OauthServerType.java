package com.formssafe.domain.oauth;

import java.util.Locale;

public enum OauthServerType {

    GOOGLE,
    DELETED;

    public static OauthServerType fromName(String type) {
        return OauthServerType.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}
