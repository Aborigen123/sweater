package com.example.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Controller
@RequestMapping("/user")
//@PreAuthorize("hasAuthority('Admin')")
public class UserController {
@Autowired private  UserService userService;


@GetMapping
@PreAuthorize("hasAuthority('Admin')")
public String userList(Model model) {
	model.addAttribute("users", userService.findAll());
	
	return "userList";
}
	

@GetMapping("{user}")
@PreAuthorize("hasAuthority('Admin')")
public String userEditForm(@PathVariable User user, Model model) {
	model.addAttribute("users", user);
	model.addAttribute("roles", Role.values());
	return "userEdit";
}


@PostMapping
@PreAuthorize("hasAuthority('Admin')")
public String userSave(@RequestParam("userId")User user,
		@RequestParam Map<String, String> form,
		@RequestParam String username){
	
	userService.saveUser(user, username, form);
	
	
	return "redirect:/user";
}

@GetMapping("/profile")
public String getProfile(Model model, @AuthenticationPrincipal User user) {
	model.addAttribute("username", user.getUsername());
	model.addAttribute("email", user.getEmail());
	
	return "profile";
}

@PostMapping("/profile")
public String updateProfile(Model model, @AuthenticationPrincipal User user,
		@RequestParam String password, 
		@RequestParam String email) {
	userService.updateProfile(user, password, email);
	System.out.println("User Update");
	return "redirect:/user/profile";
}
	
@GetMapping("subscribe/{user}")//userChannel.id
public String subscribe( @AuthenticationPrincipal User currentUser,
		@PathVariable User user) {
	
	userService.subscribe(currentUser, user);
	
	return "redirect:/user-messages/" + user.getId(); 
}

@GetMapping("unsubscribe/{user}")//userChannel.id
public String unsubscribe( @AuthenticationPrincipal User currentUser,
		@PathVariable User user) {
	
	userService.unsubscribe(currentUser, user);
	
	return "redirect:/user-messages/" + user.getId(); 
}

@GetMapping("{type}/{user}/list")
public String userList(
		Model model,
		@PathVariable User user,
		@PathVariable String type) {
	
	model.addAttribute("userChannel", user);
	model.addAttribute("type", type);
	
	if("subscriptions".equals(type)) {
		model.addAttribute("users", user.getSubscription());
	}else {
		model.addAttribute("users", user.getSubscribers());
	}
	return "subscriptions";
}

}
