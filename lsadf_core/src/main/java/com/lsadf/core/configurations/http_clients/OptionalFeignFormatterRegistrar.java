package com.lsadf.core.configurations.http_clients;

import java.util.Optional;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

public class OptionalFeignFormatterRegistrar implements FormatterRegistrar {

  @Override
  public void registerFormatters(FormatterRegistry registry) {
    registry.addConverter(
        Optional.class,
        String.class,
        optional -> optional.isPresent() ? optional.get().toString() : null);
  }
}
