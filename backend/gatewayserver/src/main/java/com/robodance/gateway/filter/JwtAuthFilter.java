package com.robodance.gateway.filter;

import com.robodance.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

	private static final List<String> PUBLIC_PREFIXES = List.of(
			"/api/auth/",
			"/actuator");

	private final JwtProperties jwtProperties;

	public JwtAuthFilter(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();

		if (request.getMethod() == HttpMethod.OPTIONS || isPublic(path)) {
			return chain.filter(exchange);
		}

		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		try {
			Claims claims = parseClaims(authHeader.substring(7));
			ServerHttpRequest mutated = request.mutate()
					.header("X-User-Id", claims.getSubject())
					.header("X-User-Email", String.valueOf(claims.get("email")))
					.build();
			return chain.filter(exchange.mutate().request(mutated).build());
		} catch (Exception ex) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
	}

	private boolean isPublic(String path) {
		return PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
	}

	private Claims parseClaims(String token) {
		SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	@Override
	public int getOrder() {
		return -100;
	}
}
