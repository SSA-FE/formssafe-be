package com.formssafe.domain.oauth.authcode;

import com.formssafe.domain.oauth.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}
