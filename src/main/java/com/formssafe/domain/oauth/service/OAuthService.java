package com.formssafe.domain.oauth.service;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.formssafe.domain.oauth.client.OauthMemberClientComposite;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OAuthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final UserRepository userRepository;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    @Transactional
    public User loginOrSignup(OauthServerType oauthServerType, String authCode) {
        User oauthUser = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        User user = userRepository.findByOauthId(oauthUser.oauthId())
                .orElseGet(() -> userRepository.save(oauthUser));
        log.debug("logined member: {} {}", user.id(), user.nickname());

        return user;
    }
}
