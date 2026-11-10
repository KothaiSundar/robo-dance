package com.robodance.progress.dto;

import java.util.List;

public record ProgressResponse(
		String activeLevel,
		int dailyGoalMinutes,
		int currentStreak,
		int longestStreak,
		int totalCalories,
		int totalMinutes,
		int nextUnlockedDay,
		List<HistoryItem> history) {

	public record HistoryItem(
			String level,
			int day,
			int minutes,
			int calories,
			String completedAt) {
}
}
