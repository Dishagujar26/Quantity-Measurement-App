package com.qma.user.service;

import com.qma.user.model.Role;
import com.qma.user.model.User;
import com.qma.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public Optional<User> findByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerOrUpdateGoogleUser(String googleId, String email, String name, String pictureUrl) {
        Optional<User> existingUser = userRepository.findByGoogleId(googleId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(name);
            user.setPictureUrl(pictureUrl);
            user.setLastLoginAt(LocalDateTime.now());
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

        return userRepository.save(newUser);
    }
}