package com.formssafe.global.auth;

import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.Authority;
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
        log.debug("URI: {}", request.getRequestURI());

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object userIdValue = session.getAttribute("userId");
            if (userIdValue != null) {
                Long userId = Long.parseLong(String.valueOf(userIdValue));

                UsernamePasswordAuthenticationToken authentication = createUserAuthenticationToken(userId);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("userId {} logined", userId);
            }
            log.debug("Invalid Session");
        }
        log.debug("Invalid Session");

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createUserAuthenticationToken(Long userId) {
        LoginUserDto loginUser = new LoginUserDto(userId);

        return new UsernamePasswordAuthenticationToken(loginUser, "",
                List.of(new SimpleGrantedAuthority(Authority.ROLE_USER.name())));
    }
}
