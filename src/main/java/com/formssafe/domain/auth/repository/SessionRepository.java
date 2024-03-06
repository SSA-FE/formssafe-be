package com.formssafe.domain.auth.repository;

import com.formssafe.domain.auth.entity.Session;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, String> {

    Optional<Session> findById(String id);
}
