package com.formssafe.infra.oauth.google.client;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.client.OauthMemberClient;
import com.formssafe.domain.user.entity.User;
import com.formssafe.infra.oauth.google.config.GoogleOauthConfig;
import com.formssafe.infra.oauth.google.dto.GoogleMemberResponse;
import com.formssafe.infra.oauth.google.dto.GoogleAccessToken;
import com.formssafe.infra.oauth.google.dto.GoogleRefreshToken;
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
//        GoogleAccessToken googleToken = googleApiClient.fetchToken(tokenRequestParams(code));
        GoogleAccessToken googleAccessToken = googleApiClient.fetchToken("authorization_code", googleOauthConfig.clientId(), googleOauthConfig.clientSecret(),code, googleOauthConfig.redirectUri());
//        GoogleMemberResponse googleMemberResponse = googleApiClient.fetchProfile(
//                memberRequestParams(googleToken.accessToken()));
        log.info(googleAccessToken.toString());
        GoogleMemberResponse googleMemberResponse = googleApiClient.fetchProfile(googleAccessToken.accessToken());
        googleMemberResponse.setRefreshToken(googleAccessToken.refreshToken());
        log.debug("fetch profile: {} {} {}", googleMemberResponse.getName(), googleMemberResponse.getEmail(), googleMemberResponse.getRefreshToken());
        return googleMemberResponse.toEntity();
    }
    @Override
    public void deleteAccount(String refreshToken){
        GoogleRefreshToken googleRefreshToken = googleApiClient.refreshToken("refresh_token", googleOauthConfig.clientId(), googleOauthConfig.clientSecret(), refreshToken);
        log.info(googleRefreshToken.toString());

        googleApiClient.deleteAccount(googleRefreshToken.accessToken());
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
