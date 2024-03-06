package com.formssafe.domain.auth.service;

import com.formssafe.domain.auth.entity.Session;
import com.formssafe.domain.auth.repository.SessionRepository;
import com.formssafe.domain.member.entity.Member;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public String createSession(Member member) {
        String sessionValue = UUID.randomUUID().toString().replace("-", "");
        Session session = sessionRepository.save(new Session(sessionValue, member, LocalDateTime.now()));
        log.debug("member: {}, session: {}", session.member().nickname(), session.id());
        return session.id();
    }
}
