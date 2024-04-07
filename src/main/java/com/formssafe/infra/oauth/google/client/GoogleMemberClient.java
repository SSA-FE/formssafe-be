package com.formssafe.infra.oauth.google.client;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.client.OauthMemberClient;
import com.formssafe.domain.user.entity.User;
import com.formssafe.infra.oauth.google.config.GoogleOauthConfig;
import com.formssafe.infra.oauth.google.dto.GoogleAccessTokenByCode;
import com.formssafe.infra.oauth.google.dto.GoogleAccessTokenByRefreshToken;
import com.formssafe.infra.oauth.google.dto.GoogleMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public User fetch(String code, boolean isLocal) {
        String redirectUri = googleOauthConfig.redirectUri();
        if (isLocal) {
            redirectUri = "http://localhost:5173/join/";
        }
        log.info("clientId: {}, code: {}, redirectUri: {}", googleOauthConfig.clientId(), code, redirectUri);
        GoogleAccessTokenByCode googleAccessTokenByCode = googleApiClient.fetchToken("authorization_code",
                googleOauthConfig.clientId(), googleOauthConfig.clientSecret(), code, redirectUri);
        log.info(googleAccessTokenByCode.toString());
        GoogleMemberResponse googleMemberResponse = googleApiClient.fetchProfile(googleAccessTokenByCode.accessToken());

        return googleMemberResponse.toEntity(googleAccessTokenByCode.refreshToken());
    }

    @Override
    public void deleteAccount(String refreshToken) {
        GoogleAccessTokenByRefreshToken googleAccessTokenByRefreshToken = googleApiClient.refreshToken("refresh_token",
                googleOauthConfig.clientId(), googleOauthConfig.clientSecret(), refreshToken);
        log.info(googleAccessTokenByRefreshToken.toString());

        googleApiClient.deleteAccount(googleAccessTokenByRefreshToken.accessToken());
    }
}
