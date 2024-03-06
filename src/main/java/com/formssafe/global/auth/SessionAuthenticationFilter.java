package com.formssafe.global.auth;

import com.formssafe.domain.auth.repository.SessionRepository;
import com.formssafe.domain.member.dto.LoginMember;
import com.formssafe.domain.member.entity.Authority;
import com.formssafe.domain.member.entity.Member;
import com.formssafe.global.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional<String> authCookie = CookieUtil.getCookieValue(request, "auth");
        String requestURI = request.getRequestURI();

        authCookie
                .flatMap(sessionRepository::findById)
                .ifPresent(s -> {
                    Member member = s.member();
                    UsernamePasswordAuthenticationToken authentication = createMemberAuthenticationToken(member);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("[{}]: member: {}", requestURI, member.nickname());
                });

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createMemberAuthenticationToken(Member member) {
        LoginMember loginMember = LoginMember.fromEntity(member);

        return new UsernamePasswordAuthenticationToken(loginMember, "",
                List.of(new SimpleGrantedAuthority(Authority.ROLE_USER.name())));
    }
}
