package com.robodance.progress.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CompleteWorkoutRequest(
		@NotBlank String level,
		@Min(1) @Max(30) int day,
		@Min(1) @Max(120) int minutes) {
}
