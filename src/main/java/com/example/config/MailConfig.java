package com.example.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	

	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${spring.mail.port}")
	private int port;
	
	@Value("${spring.mail.protocol}")
	private String protocol;
	
	@Value("${mail.debug}")
	private String debug;
	
	
	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSendler = new JavaMailSenderImpl();
		mailSendler.setHost(host);
		mailSendler.setPort(port);
		mailSendler.setUsername(username);
		mailSendler.setPassword(password);
		
		Properties properties = mailSendler.getJavaMailProperties();
		
	
		properties.setProperty("mail.transport.protocol", protocol);
		properties.setProperty("mail.debug", debug);
		properties.put("mail.smtp.starttls.enable", "true");
	
		
		return mailSendler;
	}

}
