package com.robodance.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "robo-dance.jwt")
public class JwtProperties {

	private String secret = "robo-dance-dev-secret-change-me-in-prod-min-32-chars";

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
