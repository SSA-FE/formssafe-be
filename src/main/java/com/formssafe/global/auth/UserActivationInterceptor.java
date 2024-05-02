package com.formssafe.global.auth;

import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
public class UserActivationInterceptor implements HandlerInterceptor {
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return false;
        }
        LoginUserDto loginUser = (LoginUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.id();
        User user = userService.getUserById(userId);

        if (!user.isActive()) {
            log.info("user is not active: " + userId);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
