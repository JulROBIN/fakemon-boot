package fr.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//	@Configuration
//	@EnableWebSecurity
//	@EnableGlobalMethodSecurity(prePostEnabled = true)
//	@Profile("!dev")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	/**
	 * ATTENTION : Penser a bien verifier que des pages du'un vieu projet traine pas ici
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/assets/**").permitAll()
		.and()
		.csrf().disable()
		.formLogin()
			.loginPage("/connect") //lien vers getmapping --> HomeController
			.loginProcessingUrl("/connect") //lien du formunaire en post !!
			.permitAll();
		
	}

	
}
