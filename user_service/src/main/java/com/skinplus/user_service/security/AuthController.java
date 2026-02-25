package com.skinplus.user_service.security;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skinplus.user_service.dto.LoginRequest;
import com.skinplus.user_service.entity.RefreshToken;
import com.skinplus.user_service.entity.User;
import com.skinplus.user_service.service.RefreshTokenService;
import com.skinplus.user_service.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	public AuthController(UserService userService, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req) {

		User user = userService.login(req.getUsername(), req.getPassword());

		RefreshToken refresh = refreshTokenService.createRefreshToken(user);

		String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());

		return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refresh.getToken()));
	}

	@PostMapping("/refresh")
	public AuthResponse refreshToken(@RequestBody RefreshRequest request) {

		String requestRefreshToken = request.getRefreshToken();

		RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);

		refreshTokenService.verifyExpiration(refreshToken);

		User user = refreshToken.getUser();

		String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());

		return new AuthResponse(newAccessToken, requestRefreshToken // same refresh token reused
		);
	}

	@PostMapping("/register")
	public User register(@RequestBody User user) {
		return userService.register(user);
	}
}