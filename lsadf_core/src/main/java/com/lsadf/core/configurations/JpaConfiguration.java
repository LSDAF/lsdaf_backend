package com.lsadf.core.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(name = "jpa.enabled", havingValue = "true")
@EnableJpaRepositories(basePackages = "com.lsadf.core.repositories")
@EntityScan(basePackages = "com.lsadf.core.entities")
public class JpaConfiguration {}
