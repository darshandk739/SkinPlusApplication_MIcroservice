package com.skinplus.user_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ← Spring's, not jakarta
import com.skinplus.user_service.entity.RefreshToken;
import com.skinplus.user_service.entity.User;
import com.skinplus.user_service.repository.RefreshTokenRepository;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Transactional // ← only here, not on the class
	public RefreshToken createRefreshToken(User user) {
		refreshTokenRepository.deleteByUser(user);

		RefreshToken token = new RefreshToken();
		token.setUser(user);
		token.setToken(UUID.randomUUID().toString());
		token.setExpiryDate(Instant.now().plusSeconds(60 * 60 * 24 * 7));

		return refreshTokenRepository.save(token);
	}

	@Transactional
	public void deleteByUser(User user) {
		refreshTokenRepository.deleteByUser(user);
	}

	public RefreshToken findByToken(String token) {
		return refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid refresh token"));
	}

	@Transactional
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.delete(token);
			throw new RuntimeException("Refresh token expired");
		}
		return token;
	}
}