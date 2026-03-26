package com.app.quantitymeasurementapp.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User management endpoints. GET /users/me → own profile (any authenticated
 * user) GET /users → all users (ADMIN only) GET /users/{id} → user by ID (ADMIN
 * only)
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "User profile and management endpoints")
public class UserController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Get my profile")
	@GetMapping("/me")
	public ResponseEntity<User> getMyProfile(@AuthenticationPrincipal User user) {
		if (user == null)
			return ResponseEntity.status(401).build();
		return ResponseEntity.ok(user);
	}

	@Operation(summary = "Get all users — ADMIN only")
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@Operation(summary = "Get user by ID — ADMIN only")
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		return userService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}