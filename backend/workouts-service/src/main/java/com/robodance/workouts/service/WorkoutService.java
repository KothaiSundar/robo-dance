package com.robodance.workouts.service;

import com.robodance.workouts.domain.DanceLevel;
import com.robodance.workouts.dto.WorkoutDetailResponse;
import com.robodance.workouts.dto.WorkoutSummaryResponse;
import com.robodance.workouts.repo.WorkoutRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WorkoutService {

	private final WorkoutRepository workoutRepository;

	public WorkoutService(WorkoutRepository workoutRepository) {
		this.workoutRepository = workoutRepository;
	}

	@Transactional(readOnly = true)
	public List<WorkoutSummaryResponse> listByLevel(DanceLevel level) {
		return workoutRepository.findByLevelOrderByDayNumberAsc(level).stream()
				.map(WorkoutMapper::toSummary)
				.toList();
	}

	@Transactional(readOnly = true)
	public WorkoutDetailResponse getByLevelAndDay(DanceLevel level, int day) {
		return workoutRepository
				.findByLevelAndDayNumber(level, day)
				.map(WorkoutMapper::toDetail)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));
	}

	public static DanceLevel parseLevel(String level) {
		try {
			return DanceLevel.valueOf(level.trim().toUpperCase());
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid level. Use BEGINNER, INTERMEDIATE, or ADVANCED");
		}
	}
}
