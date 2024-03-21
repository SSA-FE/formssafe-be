package com.formssafe.infra.oauth.google.client;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.client.OauthMemberClient;
import com.formssafe.domain.user.entity.Authority;
import com.formssafe.domain.user.entity.OauthId;
import com.formssafe.domain.user.entity.User;
import com.formssafe.infra.oauth.google.config.GoogleOauthConfig;
import com.formssafe.infra.oauth.google.dto.GoogleMemberResponse;
import com.formssafe.infra.oauth.google.dto.GoogleAccessTokenByCode;
import com.formssafe.infra.oauth.google.dto.GoogleAccessTokenByRefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

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
        GoogleAccessTokenByCode googleAccessTokenByCode = googleApiClient.fetchToken("authorization_code", googleOauthConfig.clientId(), googleOauthConfig.clientSecret(),code, googleOauthConfig.redirectUri());
//        GoogleMemberResponse googleMemberResponse = googleApiClient.fetchProfile(
//                memberRequestParams(googleToken.accessToken()));
        log.info(googleAccessTokenByCode.toString());
        GoogleMemberResponse googleMemberResponse = googleApiClient.fetchProfile(googleAccessTokenByCode.accessToken());

        return User.builder()
                .oauthId(new OauthId(googleMemberResponse.sub(), OauthServerType.GOOGLE))
                .nickname(googleMemberResponse.name())
                .email(googleMemberResponse.email())
                .imageUrl(googleMemberResponse.picture())
                .authority(Authority.ROLE_USER)
                .createTime(LocalDateTime.now())
                .refreshToken(googleAccessTokenByCode.refreshToken())
                .build();
    }
    @Override
    public void deleteAccount(String refreshToken){
        GoogleAccessTokenByRefreshToken googleAccessTokenByRefreshToken = googleApiClient.refreshToken("refresh_token", googleOauthConfig.clientId(), googleOauthConfig.clientSecret(), refreshToken);
        log.info(googleAccessTokenByRefreshToken.toString());

        googleApiClient.deleteAccount(googleAccessTokenByRefreshToken.accessToken());
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
