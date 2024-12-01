package com.lsadf.admin;

import com.lsadf.admin.configurations.LsadfAdminConfiguration;
import com.lsadf.core.utils.ApplicationUtils;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/** Main class for the admin application */
@SpringBootApplication
@Import(LsadfAdminConfiguration.class)
@Slf4j
public class LsadfAdminApplication {
  public static void main(String[] args) throws UnknownHostException {
    SpringApplication application = new SpringApplication(LsadfAdminApplication.class);
    ConfigurableApplicationContext context = application.run(args);
    ApplicationUtils.printAccessUrl(context, log);
  }
}
