package com.formssafe.util.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockSessionAuthenticationSecurityContextFactory.class)
public @interface WithMockSessionAuthentication {

    long id() default 1L;

    String role() default "ROLE_USER";

}