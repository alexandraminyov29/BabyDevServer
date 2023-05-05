package com.babydev.app.security.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean starttlsRequired;
    
	@Bean
    public JavaMailSenderImpl getNoReplyMailSender() {
		JavaMailSenderImpl noReplyMailSender = new JavaMailSenderImpl();
    	noReplyMailSender.setHost(host);
    	noReplyMailSender.setPort(port);
    	noReplyMailSender.setUsername(username);
    	noReplyMailSender.setPassword(password);

    	Properties props = noReplyMailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.starttls.required", starttlsRequired);

        return noReplyMailSender;
    }
}
