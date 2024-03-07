package com.formssafe.global.config;

import com.formssafe.infra.oauth.google.client.GoogleApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public GoogleApiClient googleApiClient() {
        return createHttpInterface(GoogleApiClient.class);
    }

    private <T> T createHttpInterface(Class<T> tClass) {
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory build = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient)).build();

        return build.createClient(tClass);
    }
}
