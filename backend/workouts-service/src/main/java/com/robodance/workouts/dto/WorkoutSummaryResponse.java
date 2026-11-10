package com.robodance.workouts.dto;

public record WorkoutSummaryResponse(
		Long id,
		String level,
		int day,
		String title,
		int durationMinutes,
		String videoUrl) {
}
