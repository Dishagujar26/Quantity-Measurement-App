package com.app.quantitymeasurementapp.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	public User registerOrUpdateUser(OAuth2User oAuth2User) {
		String googleId = oAuth2User.getAttribute("sub");
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		String pictureUrl = oAuth2User.getAttribute("picture");

		logger.info("OAuth2 login: email={}", email);

		Optional<User> existingUser = userRepository.findByGoogleId(googleId);

		if (existingUser.isPresent()) {
			User user = existingUser.get();
			user.setName(name);
			user.setPictureUrl(pictureUrl);
			user.setLastLoginAt(LocalDateTime.now());
			logger.info("Existing user logged in: {}", email);
			return userRepository.save(user);
		}

		User newUser = User.builder()
				.googleId(googleId)
				.email(email)
				.name(name)
				.pictureUrl(pictureUrl)
				.password("GOOGLE_OAUTH_USER")
				.mobile(null)
				.role(Role.USER)
				.active(true)
				.lastLoginAt(LocalDateTime.now())
				.build();

		logger.info("New user registered: {}", email);
		return userRepository.save(newUser);
	}

	public User saveUser(User user) {
		user.setGoogleId(null);
		user.setPictureUrl(null);
		user.setRole(Role.USER);
		user.setActive(true);
		user.setLastLoginAt(LocalDateTime.now());
		return userRepository.save(user);
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