package com.formssafe.domain.auth.service;

import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.SessionNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    public void createSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.debug("old session: {}", session.getId());
            session.invalidate();
        }

        session = request.getSession();
        session.setAttribute("userId", user.getId());
        log.debug("new session: {} {} {}", session.getId(), user.getId(), user.getNickname());
    }

    public void deleteSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotFoundException(ErrorCode.SESSION_NOT_FOUND, "Session cannot be null");
        }
        session.invalidate();
        log.debug("Session invalidated.");
    }
}
