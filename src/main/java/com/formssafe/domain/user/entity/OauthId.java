package com.formssafe.domain.user.entity;

import com.formssafe.domain.oauth.OauthServerType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "oauth_member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "oauth_id_unique",
                        columnNames = {
                                "oauth_server_id",
                                "oauth_server"
                        }
                ),
        }
)
public class OauthId {

    @Column(nullable = false, name = "oauth_server_id")
    private String oauthServerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "oauth_server")
    private OauthServerType oauthServerType;

    public String oauthServerId() {
        return oauthServerId;
    }

    public OauthServerType oauthServer() {
        return oauthServerType;
    }
}
