package com.formssafe.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.auth.SessionAuthenticationAccessDeniedHandler;
import com.formssafe.global.auth.SessionAuthenticationEntryPoint;
import com.formssafe.global.auth.SessionAuthenticationFilter;
import com.formssafe.global.interceptor.UserActivationInterceptor;
import com.formssafe.global.logging.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
                .requestMatchers("/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/api-docs");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(request ->
                request.requestMatchers("/v1/auth/social/**").permitAll()
                        .anyRequest().authenticated());
        http.exceptionHandling(config ->
                config.authenticationEntryPoint(new SessionAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new SessionAuthenticationAccessDeniedHandler(objectMapper)));

        http.cors(cors -> cors
                .configurationSource(corsConfigurationSource()));

        http.addFilterBefore(sessionAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(loggingFilter(),
                SessionAuthenticationFilter.class);

        http.requestCache(RequestCacheConfigurer::disable);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserActivationInterceptor(userRepository))
                .excludePathPatterns("/v1/auth/social/**",
                        "/v1/users/join", "/v1/users/profile", "/v1/users/{id}",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/api-docs");
    }

    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter();
    }

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public UserActivationInterceptor userActivationInterceptor() {
        return new UserActivationInterceptor(userRepository);
    }
}
