package config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class RestSecurityConfig {

	public static final String USER = "USER";
	public static final String ADMIN = "ADMIN";
	public static final String SUPERADMIN = "SUPERADMIN";
	public static final String URL_PATH_ACCOUNTS = "/accounts/**";

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests((authz) -> authz
						.requestMatchers(HttpMethod.DELETE, URL_PATH_ACCOUNTS).hasAnyRole(SUPERADMIN)
						.requestMatchers(HttpMethod.POST, URL_PATH_ACCOUNTS).hasAnyRole(ADMIN, SUPERADMIN)
						.requestMatchers(HttpMethod.PUT, URL_PATH_ACCOUNTS).hasAnyRole(ADMIN, SUPERADMIN)
						.requestMatchers(HttpMethod.GET, URL_PATH_ACCOUNTS).hasAnyRole("USER", ADMIN, SUPERADMIN)
						.requestMatchers(HttpMethod.GET, "/authorities").hasAnyRole("USER", ADMIN, SUPERADMIN)
						// Deny any request that doesn't match any authorization rule
						.anyRequest().denyAll())
				.httpBasic(withDefaults())
				.csrf(CsrfConfigurer::disable);

		return http.build();
	}

	//@Bean
	public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {

		UserDetails user = User.withUsername("user").password(passwordEncoder.encode("user")).roles(USER).build();
		UserDetails admin = User.withUsername("admin").password(passwordEncoder.encode("admin")).roles(USER, ADMIN).build();
		UserDetails superAdmin = User.withUsername("superadmin").password(passwordEncoder.encode("superadmin")).roles(USER, ADMIN, SUPERADMIN).build();

		return new InMemoryUserDetailsManager(user, admin, superAdmin);
	}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
