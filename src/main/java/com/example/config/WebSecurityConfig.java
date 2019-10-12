package com.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.example.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)// щоб @PreAuthorize("hasAuthority('ADMIN')") діяло
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	//@Autowired private DataSource dataSource;
	@Autowired UserService userService;
	
	@Autowired PasswordEncoder passwordEncoder;
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(8);// 8 - характеризує надійність ключа шифрування
	}
	
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/registration", "/activate/*","/static/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .rememberMe()
                .and()
            .logout()
                .permitAll();
    }



//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//             User.withDefaultPasswordEncoder()
//                .username("q")
//                .password("1")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

    
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	auth.jdbcAuthentication().dataSource(dataSource)
//	.passwordEncoder(NoOpPasswordEncoder.getInstance())
//	.usersByUsernameQuery("select username, password, active from usr where username=?")//щоб система могла найти юзера за його іменем (порядок і набір полів визначені системою)
//	.authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?");//запит помагає получити список юзерів з їх ролями
		auth.userDetailsService(userService)
		.passwordEncoder(passwordEncoder);
		System.out.println(auth.userDetailsService(userService)
				.passwordEncoder(passwordEncoder));
	}

    
}