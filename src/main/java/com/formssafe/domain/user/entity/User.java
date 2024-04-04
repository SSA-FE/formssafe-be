package com.formssafe.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Getter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OauthId oauthId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private String refreshToken;

    @Builder
    private User(Long id, OauthId oauthId, String nickname, String email, Authority authority, String imageUrl, LocalDateTime createTime, String refreshToken){
        this.id = id;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.email = email;
        this.authority = authority;
        this.imageUrl = imageUrl;
        this.createTime = createTime;
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public String toString(){
        return "[ " + id + ", " + oauthId + ", "+ nickname + ", "+ email + ", " + authority + ", "+ imageUrl + ", " + createTime + ", " + refreshToken;
    }
}
