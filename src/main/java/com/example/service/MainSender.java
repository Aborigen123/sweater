package com.example.service;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class MainSender {
	
	@Autowired
	private UserService userService;
	
@Autowired
private JavaMailSender mailSendler;

@Value("${spring.mail.username}")
private String username;

   public void send(String emailTo, String subject, String message) {
	   SimpleMailMessage mailMessage = new SimpleMailMessage();
	   
	   mailMessage.setFrom(username);
	   mailMessage.setTo(emailTo);
	   mailMessage.setSubject(subject);
	   mailMessage.setText(message);
	   
	   mailSendler.send(mailMessage);
   }
	
   
   
}
