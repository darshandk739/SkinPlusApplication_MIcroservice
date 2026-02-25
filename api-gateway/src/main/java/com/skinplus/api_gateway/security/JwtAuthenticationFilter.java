package com.skinplus.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

	@Autowired
	private JwtUtil jwtUtil;

	public JwtAuthenticationFilter() {
		super(Config.class);
	}

	public static class Config {
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {

			String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return onError(exchange.getResponse(), HttpStatus.UNAUTHORIZED,
						"Missing or invalid Authorization header");
			}

			String token = authHeader.substring(7);

			if (!jwtUtil.validateToken(token)) {
				return onError(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "Invalid or expired token");
			}

			// ✅ Optionally forward username/role as headers to downstream services
			io.jsonwebtoken.Claims claims = jwtUtil.extractClaims(token);
			exchange = exchange.mutate().request(r -> r.header("X-User-Name", claims.getSubject()).header("X-User-Role",
					claims.get("role", String.class))).build();

			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerHttpResponse response, HttpStatus status, String message) {
		response.setStatusCode(status);
		byte[] bytes = message.getBytes();
		org.springframework.core.io.buffer.DataBuffer buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}
}