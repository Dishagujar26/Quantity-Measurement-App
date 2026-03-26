package com.app.quantitymeasurementapp.config;

import com.app.quantitymeasurementapp.auth.OAuth2SuccessHandler;
import com.app.quantitymeasurementapp.jwt.JwtAuthFilter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * UC18 Security Configuration.
 *
 * Strategy: - Public paths: index, health, auth endpoints, swagger, h2-console
 * - Protected paths: all /api/v1/quantities/** (require JWT) - Admin paths:
 * /api/v1/users (require ADMIN role) - OAuth2 login via Google - JWT filter
 * validates token on every protected request - Stateless session (no
 * server-side session)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private OAuth2SuccessHandler oAuth2SuccessHandler;

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	// Paths that don't require authentication
	private static final String[] PUBLIC_PATHS = { "/", "/api/v1/quantities", "/auth/**", "/oauth2/**",
			"/login/oauth2/code/**", // ← ADD THIS
			"/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**", "/h2-console/**",
			"/actuator/health", "/actuator/info", "/error" };

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:8080"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http

				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				// Disable CSRF — stateless REST API uses JWT not sessions
				.csrf(AbstractHttpConfigurer::disable)

				// Session management — stateless, no HttpSession
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Authorization rules
				.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_PATHS).permitAll()
						.requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/api/v1/quantities/**").authenticated().anyRequest().authenticated())

				// Google OAuth2 login
				.oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler)
						.redirectionEndpoint(redir -> redir.baseUri("/login/oauth2/code/*") // ← must match Google
																							// Console
						))

				// H2 console iframe
				.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

				// JWT filter runs before Spring's UsernamePasswordAuthenticationFilter
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}