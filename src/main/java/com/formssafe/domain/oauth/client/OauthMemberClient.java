package com.formssafe.domain.oauth.client;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.user.entity.User;

public interface OauthMemberClient {

    OauthServerType supportServer();

    User fetch(String code);

    void deleteAccount(String refreshToken);
}
