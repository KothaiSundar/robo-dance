package com.robodance.progress.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record DailyGoalRequest(
		@Min(10) @Max(30) int minutes) {
}
