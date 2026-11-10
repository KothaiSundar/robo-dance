package com.robodance.workouts.service;

import com.robodance.workouts.dto.DanceStepResponse;
import com.robodance.workouts.dto.WorkoutDetailResponse;
import com.robodance.workouts.dto.WorkoutSummaryResponse;
import com.robodance.workouts.entity.Workout;
import java.util.List;

public final class WorkoutMapper {

	private WorkoutMapper() {
	}

	public static WorkoutSummaryResponse toSummary(Workout workout) {
		return new WorkoutSummaryResponse(
				workout.getId(),
				workout.getLevel().name(),
				workout.getDayNumber(),
				workout.getTitle(),
				workout.getDurationMinutes(),
				workout.getVideoUrl());
	}

	public static WorkoutDetailResponse toDetail(Workout workout) {
		List<DanceStepResponse> steps = workout.getSteps().stream()
				.map(step -> new DanceStepResponse(
						step.getStepOrder(),
						step.getName(),
						step.getCounts(),
						step.getInstruction()))
				.toList();

		return new WorkoutDetailResponse(
				workout.getId(),
				workout.getLevel().name(),
				workout.getDayNumber(),
				workout.getTitle(),
				workout.getDurationMinutes(),
				workout.getVideoUrl(),
				workout.getDescription(),
				steps);
	}
}
