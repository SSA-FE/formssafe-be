package com.formssafe.infra.oauth.google.client;

import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.client.OauthMemberClient;
import com.formssafe.infra.oauth.google.config.GoogleOauthConfig;
import com.formssafe.infra.oauth.google.dto.GoogleMemberResponse;
import com.formssafe.infra.oauth.google.dto.GoogleToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleMemberClient implements OauthMemberClient {

    private final GoogleApiClient googleApiClient;
    private final GoogleOauthConfig googleOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.GOOGLE;
    }

    @Override
    public User fetch(String code) {
        GoogleToken googleToken = googleApiClient.fetchToken(tokenRequestParams(code));
        GoogleMemberResponse googleMemberResponse = googleApiClient.fetchProfile(
                memberRequestParams(googleToken.accessToken()));
        log.debug("fetch profile: {} {}", googleMemberResponse.name(), googleMemberResponse.email());
        return googleMemberResponse.toEntity();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleOauthConfig.clientId());
        params.add("redirect_uri", googleOauthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", googleOauthConfig.clientSecret());
        return params;
    }

    private MultiValueMap<String, String> memberRequestParams(String accessToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("access_token", accessToken);
        return params;
    }
}
