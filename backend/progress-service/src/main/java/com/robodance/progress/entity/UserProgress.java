package com.robodance.progress.entity;

import com.robodance.progress.domain.DanceLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "user_progress")
public class UserProgress {

	@Id
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private DanceLevel activeLevel = DanceLevel.BEGINNER;

	@Column(nullable = false)
	private int dailyGoalMinutes = 30;

	@Column(nullable = false)
	private int currentStreak = 0;

	@Column(nullable = false)
	private int longestStreak = 0;

	@Column(nullable = false)
	private int totalCalories = 0;

	@Column(nullable = false)
	private int totalMinutes = 0;

	private LocalDate lastWorkoutDate;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public DanceLevel getActiveLevel() {
		return activeLevel;
	}

	public void setActiveLevel(DanceLevel activeLevel) {
		this.activeLevel = activeLevel;
	}

	public int getDailyGoalMinutes() {
		return dailyGoalMinutes;
	}

	public void setDailyGoalMinutes(int dailyGoalMinutes) {
		this.dailyGoalMinutes = dailyGoalMinutes;
	}

	public int getCurrentStreak() {
		return currentStreak;
	}

	public void setCurrentStreak(int currentStreak) {
		this.currentStreak = currentStreak;
	}

	public int getLongestStreak() {
		return longestStreak;
	}

	public void setLongestStreak(int longestStreak) {
		this.longestStreak = longestStreak;
	}

	public int getTotalCalories() {
		return totalCalories;
	}

	public void setTotalCalories(int totalCalories) {
		this.totalCalories = totalCalories;
	}

	public int getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(int totalMinutes) {
		this.totalMinutes = totalMinutes;
	}

	public LocalDate getLastWorkoutDate() {
		return lastWorkoutDate;
	}

	public void setLastWorkoutDate(LocalDate lastWorkoutDate) {
		this.lastWorkoutDate = lastWorkoutDate;
	}
}
