package com.formssafe.domain.user.repository;

import com.formssafe.domain.user.entity.OauthId;
import com.formssafe.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthId(OauthId oauthId);

    @Query("""
            select u from User u where u.id =:id and u.isDeleted = false 
            """)
    Optional<User> findById(@Param("id") Long id);

    boolean existsByNickname(String nickname);
}
