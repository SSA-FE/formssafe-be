package com.formssafe.domain.oauth.service;

import com.formssafe.domain.member.entity.Member;
import com.formssafe.domain.member.repository.MemberRepository;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.formssafe.domain.oauth.client.OauthMemberClientComposite;
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
    private final MemberRepository memberRepository;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    @Transactional
    public Member loginOrSignup(OauthServerType oauthServerType, String authCode) {
        Member oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        Member member = memberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> memberRepository.save(oauthMember));
        log.debug("logined member: {} {}", member.id(), member.nickname());

        return member;
    }
}
