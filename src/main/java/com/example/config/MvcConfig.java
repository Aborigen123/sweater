package com.example.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Value("${upload.path}")
	private String uploadPath;
	
	// private static String uploadPath = "D://temp//";
	
	
	@Bean
	public RestTemplate getRestTeamplate() {
		return new RestTemplate();
	}
	
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("D:/img/**")
    	//.addResourceLocations("file://" + uploadPath + "/");
    	.addResourceLocations ( uploadPath +"img"+ "/");
    }




	public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

}