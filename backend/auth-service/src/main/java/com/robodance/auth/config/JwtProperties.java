package com.robodance.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "robo-dance.jwt")
public class JwtProperties {

	private String secret = "robo-dance-dev-secret-change-me-in-prod-min-32-chars";
	private long expirationMs = 86400000L;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getExpirationMs() {
		return expirationMs;
	}

	public void setExpirationMs(long expirationMs) {
		this.expirationMs = expirationMs;
	}
}
