package com.formssafe.domain.user.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
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

    public Long id() {
        return id;
    }

    public OauthId oauthId() {
        return oauthId;
    }

    public String nickname() {
        return nickname;
    }

    public String email() {
        return email;
    }

    public Authority authority() {
        return authority;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public LocalDateTime createTime() {
        return createTime;
    }

    public String getRefreshToken(){return refreshToken;}

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public String toString(){
        return "[ " + id + ", " + oauthId + ", "+ nickname + ", "+ email + ", " + authority + ", "+ imageUrl + ", " + createTime + ", " + refreshToken;
    }
}
