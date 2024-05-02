package com.formssafe.domain.oauth.authcode;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeRequestUrlProviderComposite {

    private final Map<OauthServerType, AuthCodeRequestUrlProvider> mapping;

    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers) {
        this.mapping = providers.stream()
                .collect(Collectors.toMap(AuthCodeRequestUrlProvider::supportServer, Function.identity()));
    }

    public String provide(OauthServerType oauthServerType) {
        return getProvider(oauthServerType).provide();
    }

    private AuthCodeRequestUrlProvider getProvider(OauthServerType oauthServerType) {
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(() -> new BadRequestException(ErrorCode.UNSUPPORTED_OAUTH_TYPE,
                        "Unsupported oauth server type: " + oauthServerType.name()));
    }
}
