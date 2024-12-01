package com.lsadf.core.configurations;

import com.lsadf.core.properties.DataSourceProperties;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration class for the data source. */
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
