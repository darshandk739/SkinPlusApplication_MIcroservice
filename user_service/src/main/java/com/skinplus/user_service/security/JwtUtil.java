package com.skinplus.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	private static final String SECRET = "mysecretkeymysecretkeymysecretkey";
	private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

	private static final long EXPIRATION_TIME = 1000 * 60 * 60;

	// 🔐 create token
	public String generateToken(String username, String role) {
		return Jwts.builder().setSubject(username).claim("role", role).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(KEY, SignatureAlgorithm.HS256).compact();
	}

	// 🔐 extract username
	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	// 🔐 extract role
	public String extractRole(String token) {
		return getClaims(token).get("role", String.class);
	}

	// 🔐 VALIDATE TOKEN ← THIS is what your filter calls
	public boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return extractedUsername.equals(username) && !isTokenExpired(token);
	}

	// 🔐 expiry check
	private boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}

	// 🔐 parse claims
	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
	}
}