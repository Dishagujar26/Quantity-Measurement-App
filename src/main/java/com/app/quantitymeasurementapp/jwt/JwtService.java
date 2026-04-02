package com.app.quantitymeasurementapp.jwt;

import com.app.quantitymeasurementapp.config.AppProperties;
import com.app.quantitymeasurementapp.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Handles JWT generation and validation.
 *
 * JWT Flow: 1. User logs in with Google 2. OAuth2SuccessHandler calls
 * generateToken(user) 3. JWT is returned to client 4. Client sends JWT in
 * Authorization: Bearer <token> header 5. JwtAuthFilter validates token on
 * every request
 */
@Service
public class JwtService {

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	@Autowired
	private AppProperties appProperties;

	private SecretKey getSigningKey() {
		byte[] keyBytes = appProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Generate a JWT token for an authenticated user. Claims stored: userId, email,
	 * name, role.
	 */
	public String generateToken(User user) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + appProperties.getJwt().getExpirationMs());

		return Jwts.builder().subject(user.getEmail()).claim("userId", user.getId()).claim("name", user.getName())
				.claim("email", user.getEmail()).claim("role", user.getRole().name())
				.claim("picture", user.getPictureUrl()).issuedAt(now).expiration(expiry).signWith(getSigningKey())
				.compact();
	}

	/**
	 * Extract the email (subject) from a valid JWT.
	 */
	public String extractEmail(String token) {
		return parseClaims(token).getSubject();
	}

	/**
	 * Validate a JWT token — checks signature and expiry.
	 */
	public boolean isTokenValid(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			logger.warn("JWT expired: {}", e.getMessage());
		} catch (JwtException e) {
			logger.warn("Invalid JWT: {}", e.getMessage());
		}
		return false;
	}

	private Claims parseClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}
}