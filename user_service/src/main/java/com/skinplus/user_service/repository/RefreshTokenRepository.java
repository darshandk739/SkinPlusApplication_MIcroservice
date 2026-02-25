package com.skinplus.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.skinplus.user_service.entity.RefreshToken;
import com.skinplus.user_service.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	@Modifying
	@Transactional
	void deleteByUser(User user);

	Optional<RefreshToken> findByToken(String token);

}