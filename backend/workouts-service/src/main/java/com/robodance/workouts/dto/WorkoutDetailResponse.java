package com.robodance.workouts.dto;

import java.util.List;

public record WorkoutDetailResponse(
		Long id,
		String level,
		int day,
		String title,
		int durationMinutes,
		String videoUrl,
		String description,
		List<DanceStepResponse> steps) {
}
