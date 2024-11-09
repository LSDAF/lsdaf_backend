package com.lsadf.lsadf_backend.unit.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomJwtSecurityContextFactory.class)
public @interface WithMockJwtUser {
    String username() default "test@example.com";

    String name() default "Test User";

    String[] roles() default {"USER"};

    boolean emailVerified() default true;

    boolean enabled() default true;
}