package com.formssafe.domain.oauth.repository;

import com.formssafe.domain.oauth.entity.OauthId;
import com.formssafe.domain.oauth.entity.OauthMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthMemberRepository extends JpaRepository<OauthMember, Long> {

    Optional<OauthMember> findByOauthId(OauthId oauthId);
}
