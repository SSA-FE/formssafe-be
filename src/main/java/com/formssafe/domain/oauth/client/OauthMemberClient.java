package com.formssafe.domain.oauth.client;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.entity.OauthMember;

public interface OauthMemberClient {

    OauthServerType supportServer();

    OauthMember fetch(String code);
}
