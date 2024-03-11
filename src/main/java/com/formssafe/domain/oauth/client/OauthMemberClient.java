package com.formssafe.domain.oauth.client;

import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.oauth.OauthServerType;

public interface OauthMemberClient {

    OauthServerType supportServer();

    User fetch(String code);
}
