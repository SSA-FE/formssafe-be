package com.formssafe.global.auth;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.Authority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        authenticate(request);

        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request) {
        log.info("URI: {}", request.getRequestURI());

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                log.info("No session found.");
                return;
            }

            Object userIdValue = session.getAttribute("userId");
            if (userIdValue == null) {
                log.info("Session doesn't have userId.");
                return;
            }

            Long userId = Long.parseLong(String.valueOf(userIdValue));
            UsernamePasswordAuthenticationToken authentication = createUserAuthenticationToken(userId);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("userId {} logined", userId);
        }
    }

    private UsernamePasswordAuthenticationToken createUserAuthenticationToken(Long userId) {
        LoginUserDto loginUser = new LoginUserDto(userId);

        return new UsernamePasswordAuthenticationToken(loginUser, "",
                createAuthorityList(Authority.ROLE_USER.name()));
    }
}
