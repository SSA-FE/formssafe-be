package com.formssafe.domain.user.repository;

import com.formssafe.domain.user.entity.OauthId;
import com.formssafe.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthId(OauthId oauthId);

    Optional<User> findById(Long id);

    boolean existsByNickname(String nickname);
}
