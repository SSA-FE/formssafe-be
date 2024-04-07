package com.formssafe.domain.oauth.service;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.formssafe.domain.oauth.client.OauthMemberClientComposite;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.util.CommonUtil;
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
    //nickname 랜덤 생성용

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }


    @Transactional
    public User loginOrSignup(OauthServerType oauthServerType, String authCode, boolean isLocal) {
        User oauthUser = oauthMemberClientComposite.fetch(oauthServerType, authCode, isLocal);
        String nickname;
        do {
            nickname = CommonUtil.generateRandomNickname();
        } while (userRepository.existsByNickname(nickname));

        oauthUser.updateNickname(nickname);

        User user = userRepository.findByOauthId(oauthUser.getOauthId())
                .orElseGet(() -> userRepository.save(oauthUser));
        log.debug("logined member: {} {}", user.getId(), user.getNickname());

        return user;
    }
}
