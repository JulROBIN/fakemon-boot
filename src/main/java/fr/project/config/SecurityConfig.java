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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("!dev")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/assets/**").permitAll()
	//	.antMatchers("/medecin/**").hasAnyRole("MEDECIN", "ADMIN")
	//	.antMatchers("/secretaire/**").hasAnyRole("SECRETAIRE", "ADMIN")
	//	.antMatchers("/visite/**").hasAnyRole("SECRETAIRE", "MEDECIN", "ADMIN")
	//	.antMatchers("/patient/**").hasAnyRole("SECRETAIRE", "MEDECIN", "ADMIN")
	//	.antMatchers("/**").hasRole("ADMIN")
		.and()
		.formLogin()
			.loginPage("/connect") //lien vers getmapping --> HomeController
			.loginProcessingUrl("/connect") //lien du formunaire en post !!!
			.defaultSuccessUrl("/visite", true)
			.failureUrl("/connect?error=true")
			.permitAll();
	}

	
}
