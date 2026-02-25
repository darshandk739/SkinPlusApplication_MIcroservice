package com.skinplus.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;

@Component
public class JwtUtil {

	// ⚠️ Must match exactly what user_service uses
	private static final String SECRET = "mysecretkeymysecretkeymysecretkey";
	private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
	}
}