package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.properties.EmailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration for email
 */
@Configuration
public class EmailConfiguration {

    private static final String SMTP = "smtp";
//
//    @Bean
//    public JavaMailSender javaMailSender(EmailProperties emailProperties) {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(emailProperties.getHost());
//        mailSender.setProtocol(SMTP);
//        mailSender.setPort(emailProperties.getPort());
//        mailSender.setUsername(emailProperties.getUsername());
//        mailSender.setPassword(emailProperties.getPassword());
//
////        mailSender.setHost("smtp.office365.com");
////        mailSender.setPort(587);
////        mailSender.setUsername("lsadf_games@outlook.com");
////        mailSender.setPassword("mujdpiczxcrechkd");
//
//        Properties props = mailSender.getJavaMailProperties();
//
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.starttls.required", "true");
//        props.put("mail.debug", "true"); // Enable debug logging (optional)
//        props.put("mail.smtp.connectiontimeout", 5000); // Timeout settings
//        props.put("mail.smtp.timeout", 5000);
//        props.put("mail.smtp.writetimeout", 5000);
//        return mailSender;
//    }
}
