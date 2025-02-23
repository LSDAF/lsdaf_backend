package com.lsadf.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/** Utility class for application startup */
@UtilityClass
public class ApplicationUtils {
  private static final String SPRING_APPLICATION_NAME = "spring.application.name";
  private static final String SERVER_PORT = "server.port";

  /**
   * Print some useful data on application startup
   *
   * @param context application context
   * @param logger logger to print the data
   */
  public static void printAccessUrl(ConfigurableApplicationContext context, Logger logger)
      throws UnknownHostException {
    Environment env = context.getEnvironment();
    String protocol = "http";
    if (env.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    String appName = env.getProperty(SPRING_APPLICATION_NAME);
    String serverPort = env.getProperty(SERVER_PORT);
    String hostAddress = InetAddress.getLocalHost().getHostAddress();
    String[] profiles = env.getActiveProfiles();
    logger.info(
        """

                        ----------------------------------------------------------------------------------
                        \tApplication '{}' is running ! Access URLs:
                        \tLocal: \t\t\t{}://localhost:{}/api\t\t\t {}://localhost:{}/swagger-ui.html
                        \tExternal: \t\t{}://{}:{}/api\t\t {}://{}:{}/swagger-ui.html
                        \tProfiles(s): \t{}
                        ----------------------------------------------------------------------------------""",
        appName,
        protocol,
        serverPort,
        protocol,
        serverPort,
        protocol,
        hostAddress,
        serverPort,
        protocol,
        hostAddress,
        serverPort,
        profiles);
  }
}
