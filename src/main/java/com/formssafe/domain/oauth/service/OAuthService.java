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

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OAuthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final UserRepository userRepository;
    //nickname 랜덤 생성용
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NICKNAME_LENGTH = 8;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public String generateRandomNickname(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("user-");
        for(int i=0; i<NICKNAME_LENGTH; i++){
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Transactional
    public User loginOrSignup(OauthServerType oauthServerType, String authCode) {
        User oauthUser = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        String nickname;
        do{
            nickname = generateRandomNickname();
        }while(userRepository.existsByNickname(nickname));

        oauthUser.updateNickname(nickname);

        User user = userRepository.findByOauthId(oauthUser.oauthId())
                .orElseGet(() -> userRepository.save(oauthUser));
        log.debug("logined member: {} {}", user.id(), user.nickname());

        return user;
    }
}
