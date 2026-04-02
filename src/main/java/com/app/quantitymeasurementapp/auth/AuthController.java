package com.app.quantitymeasurementapp.auth;

import com.app.quantitymeasurementapp.config.AppProperties;
import com.app.quantitymeasurementapp.jwt.JwtService;
import com.app.quantitymeasurementapp.user.User;
import com.app.quantitymeasurementapp.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Auth endpoints:
 *
 * GET /auth/login/google → redirects to Google login page GET
 * /auth/callback/google → handled by Spring Security (not this controller) GET
 * /auth/success → called after OAuth2 success, returns JWT info GET /auth/me →
 * returns current logged-in user profile POST /auth/logout → invalidates
 * session (client deletes JWT)
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Google OAuth2 login and user profile endpoints")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AppProperties appProperties;

	/**
	 * Called after Google OAuth2 success. Returns full auth info including JWT
	 * token. The token parameter is set by OAuth2SuccessHandler redirect.
	 */
	@Operation(summary = "Auth success — returns JWT token after Google login")
	@GetMapping("/success")
	public ResponseEntity<AuthResponse> authSuccess(@RequestParam String token) {

		if (!jwtService.isTokenValid(token)) {
			return ResponseEntity.badRequest().build();
		}

		String email = jwtService.extractEmail(token);
		User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		AuthResponse authResponse = new AuthResponse(token, user.getEmail(), user.getName(), user.getPictureUrl(),
				user.getRole().name(), appProperties.getJwt().getExpirationMs());

		return ResponseEntity.ok(authResponse);
	}

	/**
	 * Returns the currently authenticated user's profile. Requires: Authorization:
	 * Bearer <jwt>
	 */
	@Operation(summary = "Get current logged-in user profile")
	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal User user) {

		if (user == null) {
			return ResponseEntity.status(401)
					.body(Map.of("error", "Not authenticated. Send Authorization: Bearer <token>"));
		}

		return ResponseEntity.ok(Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail(),
				"pictureUrl", user.getPictureUrl() != null ? user.getPictureUrl() : "", "role", user.getRole().name(),
				"active", user.isActive(), "createdAt",
				user.getCreatedAt() != null ? user.getCreatedAt().toString() : ""));
	}

	/**
	 * Logout — client should delete their JWT. Server-side: no action needed for
	 * stateless JWT.
	 */
	@Operation(summary = "Logout — client deletes JWT token")
	@PostMapping("/logout")
	public ResponseEntity<Map<String, String>> logout() {
		return ResponseEntity
				.ok(Map.of("message", "Logged out successfully. Please delete your JWT token.", "status", "success"));
	}

	/**
	 * Redirect to Google login. Spring Security handles the actual OAuth2 flow.
	 */
	@Operation(summary = "Initiate Google login — redirects to Google OAuth2")
	@GetMapping("/login/google")
	public ResponseEntity<Map<String, String>> loginWithGoogle() {
		return ResponseEntity.ok(Map.of("message", "Open this URL in your browser to login with Google", "loginUrl",
				"http://localhost:8080/oauth2/authorization/google", "note",
				"After login you will be redirected to /auth/success?token=<jwt>"));
	}
}