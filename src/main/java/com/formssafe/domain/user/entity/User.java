package com.formssafe.domain.user.entity;

import com.formssafe.global.entity.BaseTimeEntity;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Getter
@EqualsAndHashCode(callSuper = true)
public class User extends BaseTimeEntity implements Serializable {
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
    private String refreshToken;

    private boolean isActive;

    private boolean isDeleted;

    @Builder
    private User(Long id, OauthId oauthId, String nickname, String email, Authority authority, String imageUrl,
                 String refreshToken, boolean isActive, boolean isDeleted) {
        this.id = id;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.email = email;
        this.authority = authority;
        this.imageUrl = imageUrl;
        this.refreshToken = refreshToken;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void deleteUser(String nickname, String email, OauthId oauthId) {
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.email = email;
        this.imageUrl = "DELETED";
        this.refreshToken = "DELETED";
        this.isDeleted = true;
    }

    public void activate() {
        this.isActive = true;
    }

    public String toString() {
        return "[ " + id + ", " + oauthId + ", " + nickname + ", " + email + ", " + authority + ", " + imageUrl + ", "
                + refreshToken;
    }
}
