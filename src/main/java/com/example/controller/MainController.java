package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.MessageRepository;
import com.example.repository.UserRepository;


@Controller
public class MainController {

	@Autowired private MessageRepository messageRepository;
	@Autowired private UserRepository userRepository;
	
	@Value("${upload.path}")
	private String uploadPath;
	// private static String uploadPath = "D://temp//";
	
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    @GetMapping("/")
    public String greeting2(@RequestParam(name="name", required=false, defaultValue="World") String name, Map<String, Object> model) {
        model.put("name", name);
        return "greeting3";
    }
    
    @GetMapping("/main")
    public String main(@RequestParam(required = false) String filter, Model model) {
    	Iterable<Message> messages = messageRepository.findAll();

        if(filter == null || filter.isEmpty() || filter.equals("")) {
        	messages = messageRepository.findAll();
        }else {
        	 messages = messageRepository.findByTag(filter);
        }
     	
    	
    	model.addAttribute("messages", messages);
    	model.addAttribute("some", "h1 Vito");
    	model.addAttribute("filter", filter);
    	return "main";
    }

    @PostMapping("/main")
    public String add(
    		@AuthenticationPrincipal User user,
    	//	@RequestParam String text, @RequestParam String tag, 
            @Valid  Message message, 		//порядок написання 
            BindingResult bindingResult, //список аргументів і повідомлень помилок валідацій
    	//	Map<String, Object> model,
    		Model model,
            @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
    	
    	//Message message = new Message(text, tag, user);
    	
    	message.setAuthor(user);
    	
    	if(bindingResult.hasErrors()) {
    		
    	//	bindingResult.getFieldErrors().stream().collect(Collectors.toMap(fieldError -> fieldError.getField() + "Error", FieldError::getDefaultMessage));
    		Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
    		
    		model.mergeAttributes(errorsMap);
             model.addAttribute("message", message);
    	}else {
    	
    	  if (file != null && !file.getOriginalFilename().isEmpty()) {
              File uploadDir = new File(uploadPath);
    		
    		if(!uploadDir.exists()) {
    			uploadDir.mkdir();
    		}
    		String uuidFile = UUID.randomUUID().toString();
    		String resultFilename = uuidFile + "." + file.getOriginalFilename();
    		
    		System.out.println("READ = " + uploadPath +"img"+ "/" + resultFilename);
    		file.transferTo(new File( uploadPath  +"img"+ "/"+ resultFilename));//загрузка файлу
    		
    		message.setFilename(resultFilename);
    	}
    
    	  model.addAttribute("message", null);
    	  
    	messageRepository.save(message);
    	}
    	
    	Iterable<Message> messages = messageRepository.findAll();
    	
    	model.addAttribute("messages", messages);
    	model.addAttribute("some", "h1 Vito");
    	return "main";
    }

    
    @GetMapping("/user-messages/{user}")//currentUserId
    public String userMessages(@AuthenticationPrincipal User currentUser,
    		@PathVariable User user, Model model,
    		@RequestParam(required = false) Message message
    		) {//@PathVariable(name = "currentUserId")String userId
    //	Long id = Long.parseLong(userId);
    	
    	if(user == null || "".equals(user)) {
    		System.out.println("user empty");
    	}else {
    		
    	}
    	if(currentUser == null || "".equals(currentUser)) {
    		System.out.println("currentUser empty");
    	}else {
    		
    	}
    	
    	System.out.println("This work " + user.getUsername());
    //	System.out.println("This message " + message.getId());
    //	User user = userRepository.findUserById(id);
    	System.out.println("currentUser " + currentUser);
    	Set<Message> messages = user.getMessages();
    	

    	model.addAttribute("subscribersCount", user.getSubscribers().size());
    	model.addAttribute("subscriptionsCount", user.getSubscription().size());
    	model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
    	model.addAttribute("userChannel", user);
    	model.addAttribute("messages", messages);
    	model.addAttribute("message", message);
     	model.addAttribute("isCurrentUser", currentUser.equals(user));//в User генеруємо equals and hashCode    	
     	
    	return "userMessages";
    }
    
    
    
    @PostMapping("/user-messages/{user}")
    public String changeMessage(@AuthenticationPrincipal User currentUser,
    		@PathVariable Long user,
    		@RequestParam("id")Message message,
    		@RequestParam("text") String text,
    		@RequestParam("tag")String tag,
    		@RequestParam("file") MultipartFile file
    		) throws IllegalStateException, IOException {
    	
    	if(message.getAuthor().equals(currentUser)) {
    		if(!StringUtils.isEmpty(text)) {
    			message.setText(text);
    		}
    		if(!StringUtils.isEmpty(tag)) {
    			message.setTag(tag);
    		}
    	}
    	  if (file != null && !file.getOriginalFilename().isEmpty()) {
              File uploadDir = new File(uploadPath);
    		
    		if(!uploadDir.exists()) {
    			uploadDir.mkdir();
    		}
    		String uuidFile = UUID.randomUUID().toString();
    		String resultFilename = uuidFile + "." + file.getOriginalFilename();
    		
    		System.out.println("READ = " + uploadPath +"img"+ "/" + resultFilename);
    		file.transferTo(new File( uploadPath  +"img"+ "/"+ resultFilename));//загрузка файлу
    		
    		message.setFilename(resultFilename);
    	  }
    	messageRepository.save(message);
    	return "redirect:/user-message/" + user;
    }
    
 
}