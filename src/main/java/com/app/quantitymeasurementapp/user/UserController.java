package com.app.quantitymeasurementapp.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "User profile and management endpoints")
public class UserController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Create a new user")
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User savedUser = userService.saveUser(user);
		return ResponseEntity.ok(savedUser);
	}

	@Operation(summary = "Find user by email")
	@GetMapping("/email/{email}")
	public ResponseEntity<User> findUserByEmail(@PathVariable String email) {
		Optional<User> user = userService.findByEmail(email);
		return user.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Operation(summary = "Get my profile")
	@GetMapping("/me")
	public ResponseEntity<User> getMyProfile(@AuthenticationPrincipal User user) {
		if (user == null) {
			return ResponseEntity.status(401).build();
		}
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