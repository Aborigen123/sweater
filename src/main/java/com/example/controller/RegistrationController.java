package com.example.controller;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.domain.User;
import com.example.domain.dto.CaptchaResponseDto;
import com.example.service.UserService;

@Controller
public class RegistrationController {

	private final static String CAPTCHA_URL  = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
	
	@Autowired private UserService userService;
	
	@Value("${recaptcha.secret}")
	private String secret;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/registration")
	public String registration(Model model) {
		
	
		return "registration";
	}
	
	@PostMapping("/registration")
	public String addUser(@RequestParam("password2") String passwordConfirmation,@RequestParam("g-recaptcha-response")String captchaResponce, @Valid User user, BindingResult bindingResult, Model model) {
		
		String url = String.format(CAPTCHA_URL, secret, captchaResponce);
		
		CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
		
		if(!response.isSuccess()) {
			model.addAttribute("captchaError", "Fail captcha");
		}
		
		boolean isConfirmEntity = StringUtils.isEmpty(passwordConfirmation);
		
		if(isConfirmEntity) {
			model.addAttribute("password2Error", "Password confirmation cannot be empty");
		}
		
		boolean equals = user.getPassword() !=null && !user.getPassword().equals(passwordConfirmation) && !isConfirmEntity; 
	if(equals) {
		model.addAttribute("passwordError", "Password are different");
		model.addAttribute("password2Error", "Password and Password confirmation are different");
		System.out.println("passwordError ---------------------------------------------------------------------------------------------------------------------------" + "Password are different");
		
	}
	
	if(isConfirmEntity || bindingResult.hasErrors() || equals || !response.isSuccess()) {
	Map<String, String > errors =	ControllerUtils.getErrors(bindingResult);
	
	model.mergeAttributes(errors); //mergeAttributes - !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	return "registration";
	}
		
		if(!userService.addUser(user)) {
			model.addAttribute("usernameError", "User exists");
			return "registration";
		}
	
		return "redirect:/login";
	}
	
	@GetMapping("/activate/{code}")
	   public String activate(Model model, @PathVariable String code) {
		   boolean isActived = userService.activateUser(code);
		   System.out.println("code -------------------------------------------------------------------------------- " + code);
		   if(isActived) {
			  model.addAttribute("messageType", "success");
			   model.addAttribute("message", "User successfully activated");
		   }else {
				  model.addAttribute("messageType", "danger");
			   model.addAttribute("message","Activated code is not found");
		   }
		   
		   return "login";
	   }
}
