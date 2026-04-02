package com.app.quantitymeasurementapp.auth;

import com.app.quantitymeasurementapp.jwt.JwtService;
import com.app.quantitymeasurementapp.user.User;
import com.app.quantitymeasurementapp.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Called by Spring Security after successful Google OAuth2 login.
 *
 * Steps: 1. Extract Google user info from OAuth2User 2. Register or update user
 * in our DB 3. Generate JWT token 4. Redirect client to
 * /auth/success?token=<jwt>
 *
 * The client (frontend/Insomnia) then stores the JWT and sends it in
 * Authorization: Bearer header for all subsequent requests.
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

		// Register/update user in DB
		User user = userService.registerOrUpdateUser(oAuth2User);
		logger.info("Google login successful for: {}", user.getEmail());

		// Generate JWT
		String jwt = jwtService.generateToken(user);

		// Redirect with JWT as query param
		// In production: redirect to frontend URL with token
		String redirectUrl = "/auth/success?token=" + jwt;
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}