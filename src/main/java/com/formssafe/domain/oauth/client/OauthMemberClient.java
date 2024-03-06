package com.formssafe.domain.oauth.client;

import com.formssafe.domain.member.entity.Member;
import com.formssafe.domain.oauth.OauthServerType;

public interface OauthMemberClient {

    OauthServerType supportServer();

    Member fetch(String code);
}
