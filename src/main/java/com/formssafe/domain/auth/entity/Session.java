package com.formssafe.domain.auth.entity;

import com.formssafe.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDateTime createTime;

    public String id() {
        return id;
    }

    public Member member() {
        return member;
    }

    public LocalDateTime createTime() {
        return createTime;
    }
}
