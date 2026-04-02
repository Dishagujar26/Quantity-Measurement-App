package com.app.quantitymeasurementapp.jwt;

import com.app.quantitymeasurementapp.user.User;
import com.app.quantitymeasurementapp.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * JWT Authentication Filter.
 *
 * Runs ONCE per request (OncePerRequestFilter). Extracts JWT from Authorization
 * header, validates it, loads the user, and sets the Spring Security context.
 *
 * Flow: Request → JwtAuthFilter → validates JWT → sets SecurityContext →
 * Controller
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = extractToken(request);

		if (token != null && jwtService.isTokenValid(token)) {
			String email = jwtService.extractEmail(token);
			Optional<User> userOpt = userService.findByEmail(email);

			if (userOpt.isPresent() && userOpt.get().isActive()) {
				User user = userOpt.get();

				// Build Spring Security authentication object
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
						List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

				SecurityContextHolder.getContext().setAuthentication(auth);
				logger.debug("Authenticated user: {}", email);
			}
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Extract JWT from Authorization: Bearer <token> header.
	 */
	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
}