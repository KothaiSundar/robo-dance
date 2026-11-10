package com.robodance.progress.dto;

public record BadgeResponse(
		String code,
		String title,
		String description,
		boolean earned,
		String earnedAt) {
}
