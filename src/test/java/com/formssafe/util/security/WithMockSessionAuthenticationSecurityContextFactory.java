package com.formssafe.util.security;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockSessionAuthenticationSecurityContextFactory implements
        WithSecurityContextFactory<WithMockSessionAuthentication> {

    @Override
    public SecurityContext createSecurityContext(WithMockSessionAuthentication annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new LoginUserDto(annotation.id()), "",
                createAuthorityList(annotation.role())
        );

        context.setAuthentication(authentication);
        return context;
    }
}