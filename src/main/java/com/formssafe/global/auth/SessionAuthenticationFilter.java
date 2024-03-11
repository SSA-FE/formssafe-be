package com.formssafe.global.auth;

import com.formssafe.domain.user.dto.LoginUser;
import com.formssafe.domain.user.entity.Authority;
import com.formssafe.domain.user.entity.User;
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
                User user = (User) memberValue;

                UsernamePasswordAuthenticationToken authentication = createMemberAuthenticationToken(user);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("[{}]: member: {}", request.getRequestURI(), user.nickname());
            }
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createMemberAuthenticationToken(User user) {
        LoginUser loginUser = LoginUser.fromEntity(user);

        return new UsernamePasswordAuthenticationToken(loginUser, "",
                List.of(new SimpleGrantedAuthority(Authority.ROLE_USER.name())));
    }
}
