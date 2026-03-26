package com.app.quantitymeasurementapp.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response body returned after successful authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

	private String token;
	private String tokenType = "Bearer";
	private String email;
	private String name;
	private String pictureUrl;
	private String role;
	private long expiresInMs;

	public AuthResponse(String token, String email, String name, String pictureUrl, String role, long expiresInMs) {
		this.token = token;
		this.tokenType = "Bearer";
		this.email = email;
		this.name = name;
		this.pictureUrl = pictureUrl;
		this.role = role;
		this.expiresInMs = expiresInMs;
	}
}