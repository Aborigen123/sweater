package com.example.sweater;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.controller.MainController;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MainController mainController;
	
	@Test
	public void contextLoads() throws Exception{
	//	assertThat(mainController).isNotNull();
	this.mockMvc.perform(get("/"))//get запит по силці "/"
	.andDo(print())//виводить результат в консоль
	.andExpect(status().isOk())//порівняти результат  з тим яким ми очікуємо (200)
	.andExpect(content().string(containsString("Main page")));//верне контент і порівнюємо чи має він в собі "Hello World"
	}
	
	
	@Test
	public void accessDeniedTest() throws Exception{//тест для провірки авторизації
      this.mockMvc.perform(get("/main"))
      .andDo(print())
      .andExpect(status().is3xxRedirection())//статус 302 перенаправлення на іншу силку
      .andExpect(redirectedUrl("http://localhost/login"));//силка куда перенаправляє
	}
	
	@Test
	public void correctLoginTest() throws Exception{//перевірка авторизації користувача
		this.mockMvc.perform(formLogin().user("qwe").password("123"))
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/"));
	}
	
	
	@Test
	public void badCredentials() throws Exception{
		this.mockMvc.perform(post("/login").param("user", "Alfred"))
		.andDo(print())
		.andExpect(status().isForbidden()); //403 status
	}
	
	}
