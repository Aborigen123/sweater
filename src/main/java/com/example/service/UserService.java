package com.example.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired private UserRepository userRepository;
	
   @Autowired private MainSender mailSender;
   
	@Autowired  private PasswordEncoder passwordEncoder;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	User user =	 userRepository.findByUsername(username);
		
	if(user == null) {
		throw new UsernameNotFoundException("User not found");
	}
	
		 return user;
	}

	public boolean addUser(User user) {
		
		User userFromDb = userRepository.findByUsername(user.getUsername());
		
		if(userFromDb != null) {
			return false;
		}
		
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.User));
		
		user.setActivationCode(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userRepository.save(user);
		System.out.println("reg is success" );
		
		if(!StringUtils.isEmpty(user.getEmail())) {
			 String message = String.format(
	                    "Hello, %s! \n" +
	                            "Welcome to Sweater. Please, visit next link: http://localhost:8080/activate/%s",
	                    user.getUsername(),
	                    user.getActivationCode()
	                    );
		
			 
			  mailSender.send(user.getEmail(), "Activation code", message);
		}
		
		return true;
	}

	public  boolean activateUser(String code) {
   User user = userRepository.findByActivationCode(code);
   
   if(user == null) {
	   return false;
   }
   
   user.setActivationCode(null);
   
   System.out.println("user -----------------------------------------------------------------------------------------------------" + user.getEmail());
   userRepository.save(user);
		
		return true;
	}

	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	public void saveUser(User user, String username, Map<String, String> form) {
		user.setUsername(username);
		
		Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
		
		
		user.getRoles().clear();
		
		for(String key : form.keySet()) {
			System.out.println("key = " + key);
			if(roles.contains(key)) {
				user.getRoles().add(Role.valueOf(key));
			}
		}
		
		
		userRepository.save(user);
		
	}

	public void updateProfile(User user, String password, String email) {
	  String userEmail = user.getEmail();
System.out.println("updateProfile");
	  boolean isEmailChange = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email));
	  
	  if(isEmailChange) {
		  user.setEmail(email);
		  
		  
		  if(!StringUtils.isEmpty(email)) {
			  user.setActivationCode(UUID.randomUUID().toString());
		  }
	  }
	  
	  if(!StringUtils.isEmpty(password)) {
		  user.setPassword(password);
	  }
	  
	  userRepository.save(user);
	  
	  if(isEmailChange) {
		if(!StringUtils.isEmpty(user.getEmail())) {
			 String message = String.format(
	                    "Hello, %s! \n" +
	                            "Welcome to Sweater. Please, visit next link: http://localhost:8080/activate/%s",
	                    user.getUsername(),
	                    user.getActivationCode()
	                    );
		
			 
			  mailSender.send(user.getEmail(), "Activation code", message);
		}
	  }
	  
		
	}

	public void subscribe(User currentUser, User user) {
	user.getSubscribers().add(currentUser);
		
	
	userRepository.save(user);
	}

	public void unsubscribe(User currentUser, User user) {
		user.getSubscribers().remove(currentUser);
		
		userRepository.save(user);
		
	}




}
