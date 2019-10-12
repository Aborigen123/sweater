package com.example.sweater;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.controller.MainController;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("qwe")
public class MainControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private MainController mainController;
	
	@Test
	public void mainPageTest() throws Exception{//для того щоб перейти по цый силці нам треба залогінетися анотація над класом @WithUserDetails("qwe")
		this.mockMvc.perform(get("/main"))
		.andDo(print())
		.andExpect(authenticated())
		.andExpect(xpath("//*[@id=\"navbarSupportedContent\"]/div").string("qwe")); //copy -> xpath ("//*[@id=\"navbarSupportedContent\"]/div") || перевіряємо чи імя яке висвітлюється вверху буде == тому імені яке ми тут передали 
	}
	
	
/*	@Test//перевіряємо коректне відображення списку повідомлень
	public void messagelistTest() throws Exception{
		this.mockMvc.perform(get("/main"))
		.andDo(print())
		.andExpect(authenticated())//перевіряємло чи користувач залогінений
		.andExpect(xpath("").nodeCount(0));//поверне кількість вузлів
    
	}*/
	
}
