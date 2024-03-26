package com.formssafe.domain.auth.service;

import com.formssafe.domain.user.entity.User;
import com.formssafe.global.exception.type.SessionNotFoundException;
import com.formssafe.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    public void createSession(HttpServletRequest request, HttpServletResponse response, User user) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.debug("old session: {}", session.getId());
            session.invalidate();
        }

        session = request.getSession();
        session.setAttribute("userId", user.id());
        log.debug("new session: {} {} {}", session.getId(), user.id(), user.nickname());

        CookieUtil.addCookie(response, "SESSION_ID", session.getId(), "api.formssafe.com");
    }

    public void deleteSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException("Session cannot be null");
        }
        session.invalidate();
        log.debug("Session invalidated.");

        CookieUtil.deleteCookie(request, response, "SESSION_ID");
    }
}
