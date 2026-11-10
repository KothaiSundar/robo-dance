package com.robodance.workouts.dto;

public record DanceStepResponse(
		int order,
		String name,
		String counts,
		String instruction) {
}
