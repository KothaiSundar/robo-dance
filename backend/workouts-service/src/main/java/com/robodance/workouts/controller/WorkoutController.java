package com.robodance.workouts.controller;

import com.robodance.workouts.domain.DanceLevel;
import com.robodance.workouts.dto.WorkoutDetailResponse;
import com.robodance.workouts.dto.WorkoutSummaryResponse;
import com.robodance.workouts.service.WorkoutService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

	private final WorkoutService workoutService;

	public WorkoutController(WorkoutService workoutService) {
		this.workoutService = workoutService;
	}

	@GetMapping
	public List<WorkoutSummaryResponse> list(@RequestParam String level) {
		DanceLevel danceLevel = WorkoutService.parseLevel(level);
		return workoutService.listByLevel(danceLevel);
	}

	@GetMapping("/{level}/{day}")
	public WorkoutDetailResponse detail(@PathVariable String level, @PathVariable int day) {
		if (day < 1 || day > 30) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day must be between 1 and 30");
		}
		return workoutService.getByLevelAndDay(WorkoutService.parseLevel(level), day);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, String>> handle(ResponseStatusException ex) {
		return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", String.valueOf(ex.getReason())));
	}
}
