package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.properties.DataSourceProperties;
import com.lsadf.lsadf_backend.properties.DbInitProperties;
import com.lsadf.lsadf_backend.services.UserService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/**
 * Configuration class for the data source.
 */
@Configuration
public class DataSourceConfiguration {


    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dataSourceProperties.getUrl());
        dataSourceBuilder.username(dataSourceProperties.getUsername());
        dataSourceBuilder.password(dataSourceProperties.getPassword());

        return dataSourceBuilder.build();
    }
}
