package com.robodance.workouts.entity;

import com.robodance.workouts.domain.DanceLevel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workouts", uniqueConstraints = @UniqueConstraint(columnNames = {"level", "dayNumber"}))
public class Workout {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private DanceLevel level;

	@Column(nullable = false)
	private int dayNumber;

	@Column(nullable = false, length = 160)
	private String title;

	@Column(nullable = false)
	private int durationMinutes;

	@Column(nullable = false, length = 500)
	private String videoUrl;

	@Column(nullable = false, length = 400)
	private String description;

	@OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("stepOrder ASC")
	private List<DanceStep> steps = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public DanceLevel getLevel() {
		return level;
	}

	public void setLevel(DanceLevel level) {
		this.level = level;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DanceStep> getSteps() {
		return steps;
	}

	public void setSteps(List<DanceStep> steps) {
		this.steps = steps;
	}

	public void addStep(DanceStep step) {
		steps.add(step);
		step.setWorkout(this);
	}
}
