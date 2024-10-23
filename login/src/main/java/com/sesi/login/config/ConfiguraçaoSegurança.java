package com.sesi.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sesi.login.service.DetalhesDoUsuarioService;

@Configuration
public class ConfiguraçaoSegurança {
	
	private final DetalhesDoUsuarioService detalhesDoUsuarioService;
	
	public ConfiguraçaoSegurança(DetalhesDoUsuarioService detalhesDoUsuarioService) {
		this.detalhesDoUsuarioService = detalhesDoUsuarioService;
	}
	
	@Bean
	public BCryptPasswordEncoder encoderSenha() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests((authorize) ->
		authorize
		.requestMatchers("/login","/registrar","/h2-console/**").permitAll() //se for o login deixa
		.requestMatchers("/css/**").permitAll()
		.anyRequest().authenticated() //qualquer outra coisa não
		)
		.formLogin((form ) ->
		form
		.loginPage("/login")
		.defaultSuccessUrl("/home",true)
		.permitAll()
		)
        .logout((logout) -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()
		);
		http.csrf().disable();
		http.headers().frameOptions().disable();
		return http.build();
	}
}