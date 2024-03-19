package com.formssafe.domain.user.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Setter
@Builder
@AllArgsConstructor
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
    public String toString(){
        return "[ " + id + ", " + oauthId + ", "+ nickname + ", "+ email + ", " + authority + ", "+ imageUrl + ", " + createTime;
    }
}
