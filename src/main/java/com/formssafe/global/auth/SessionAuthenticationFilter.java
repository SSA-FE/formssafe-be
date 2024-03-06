package com.formssafe.global.auth;

import com.formssafe.domain.member.dto.LoginMember;
import com.formssafe.domain.member.entity.Authority;
import com.formssafe.domain.member.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object memberValue = session.getAttribute("member");
            if (memberValue != null) {
                Member member = (Member) memberValue;

                UsernamePasswordAuthenticationToken authentication = createMemberAuthenticationToken(member);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("[{}]: member: {}", request.getRequestURI(), member.nickname());
            }
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createMemberAuthenticationToken(Member member) {
        LoginMember loginMember = LoginMember.fromEntity(member);

        return new UsernamePasswordAuthenticationToken(loginMember, "",
                List.of(new SimpleGrantedAuthority(Authority.ROLE_USER.name())));
    }
}
