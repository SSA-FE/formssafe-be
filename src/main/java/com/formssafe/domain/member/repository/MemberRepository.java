package com.formssafe.domain.member.repository;

import com.formssafe.domain.member.entity.Member;
import com.formssafe.domain.member.entity.OauthId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthId(OauthId oauthId);

    Optional<Member> findById(Long id);
}
