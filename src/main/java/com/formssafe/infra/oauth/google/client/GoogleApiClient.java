package com.formssafe.infra.oauth.google.client;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.formssafe.infra.oauth.google.dto.GoogleAccessTokenByCode;
import com.formssafe.infra.oauth.google.dto.GoogleAccessTokenByRefreshToken;
import com.formssafe.infra.oauth.google.dto.GoogleMemberResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface GoogleApiClient {

    @PostExchange(url = "https://oauth2.googleapis.com/token",
            contentType = APPLICATION_FORM_URLENCODED_VALUE)
    GoogleAccessTokenByCode fetchToken(@RequestParam("grant_type") String grantType,
                                       @RequestParam("client_id") String clientId,
                                       @RequestParam("client_secret") String clientSecret,
                                       @RequestParam("code") String authCode,
                                       @RequestParam("redirect_uri") String redirectUri);

    @PostExchange(url = "https://oauth2.googleapis.com/token",
        contentType =APPLICATION_FORM_URLENCODED_VALUE)
    GoogleAccessTokenByRefreshToken refreshToken(@RequestParam("grant_type") String grantType,
                                                 @RequestParam("client_id") String clientId,
                                                 @RequestParam("client_secret") String clientSecret,
                                                 @RequestParam("refresh_token") String refreshToken);

    @PostExchange(url="https://accounts.google.com/o/oauth2/revoke",
            contentType = APPLICATION_FORM_URLENCODED_VALUE)
    void deleteAccount(@RequestParam("token") String accessToken);

    @GetExchange(url = "https://www.googleapis.com/oauth2/v3/userinfo",
            accept = APPLICATION_JSON_VALUE)
    GoogleMemberResponse fetchProfile(@RequestParam("access_token") String accessToken);
}
