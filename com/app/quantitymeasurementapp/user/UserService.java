package com.app.quantitymeasurementapp.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Handles user registration and profile management. Called after successful
 * Google OAuth2 authentication.
 */
@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	/**
	 * Register a new user or update existing user after Google login. First login →
	 * create user with USER role. Subsequent logins → update lastLoginAt.
	 */
	public User registerOrUpdateUser(OAuth2User oAuth2User) {
		String googleId = oAuth2User.getAttribute("sub");
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		String pictureUrl = oAuth2User.getAttribute("picture");

		logger.info("OAuth2 login: email={}", email);

		Optional<User> existingUser = userRepository.findByGoogleId(googleId);

		if (existingUser.isPresent()) {
			// Update existing user
			User user = existingUser.get();
			user.setName(name);
			user.setPictureUrl(pictureUrl);
			user.setLastLoginAt(LocalDateTime.now());
			logger.info("Existing user logged in: {}", email);
			return userRepository.save(user);
		}

		// Register new user
		User newUser = User.builder().googleId(googleId).email(email).name(name).pictureUrl(pictureUrl).role(Role.USER)
				.active(true).lastLoginAt(LocalDateTime.now()).build();

		logger.info("New user registered: {}", email);
		return userRepository.save(newUser);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public java.util.List<User> getAllUsers() {
		return userRepository.findAll();
	}
}