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

    @Bean
    public JavaMailSender javaMailSender(EmailProperties emailProperties) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getHost());
        mailSender.setPort(emailProperties.getPort());
        mailSender.setUsername(emailProperties.getUsername());
        mailSender.setPassword(emailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", emailProperties.isAuth());
        props.put("mail.smtp.starttls.enable", emailProperties.isStarttlsEnabled());
        props.put("mail.smtp.starttls.required", emailProperties.isStarttlsRequired());
        props.put("mail.debug", emailProperties.isDebug());
        props.put("mail.smtp.connectiontimeout", emailProperties.getConnectionTimeout());
        props.put("mail.smtp.timeout", emailProperties.getTimeout());
        props.put("mail.smtp.writetimeout", emailProperties.getWriteTimeout());

        return mailSender;
    }
}
