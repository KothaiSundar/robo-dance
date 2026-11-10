package com.robodance.auth.service;

import com.robodance.auth.config.JwtProperties;
import com.robodance.auth.entity.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final JwtProperties properties;

	public JwtService(JwtProperties properties) {
		this.properties = properties;
	}

	public String createToken(UserAccount user) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + properties.getExpirationMs());
		return Jwts.builder()
				.subject(String.valueOf(user.getId()))
				.claim("email", user.getEmail())
				.claim("displayName", user.getDisplayName())
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey())
				.compact();
	}

	private SecretKey secretKey() {
		return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
	}
}
