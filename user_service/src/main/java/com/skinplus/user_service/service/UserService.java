package com.skinplus.user_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skinplus.user_service.entity.User;
import com.skinplus.user_service.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final UserRepository repo;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
	}

	// 🔐 Registration
	public User register(User user) {

		// default role
		user.setRole("USER");

		// encrypt password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return repo.save(user);
	}

	// 🔐 Login validation (no token generation here)
	@Transactional
	public User login(String username, String rawPassword) {

		System.out.println("LOGIN METHOD RECEIVED USERNAME = [" + username + "]");
		System.out.println("LOGIN METHOD RECEIVED PASSWORD = [" + rawPassword + "]");

		User user = repo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new RuntimeException("Invalid password");
		}

		return user;
	}
}