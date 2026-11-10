package com.robodance.progress.controller;

import com.robodance.progress.dto.BadgeResponse;
import com.robodance.progress.dto.CompleteWorkoutRequest;
import com.robodance.progress.dto.DailyGoalRequest;
import com.robodance.progress.dto.LevelRequest;
import com.robodance.progress.dto.ProgressResponse;
import com.robodance.progress.service.ProgressService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

	private final ProgressService progressService;

	public ProgressController(ProgressService progressService) {
		this.progressService = progressService;
	}

	@GetMapping
	public ProgressResponse get(@RequestHeader("X-User-Id") Long userId) {
		return progressService.getProgress(userId);
	}

	@PutMapping("/level")
	public ProgressResponse setLevel(
			@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody LevelRequest request) {
		return progressService.setLevel(userId, request.level());
	}

	@PutMapping("/daily-goal")
	public ProgressResponse setDailyGoal(
			@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody DailyGoalRequest request) {
		return progressService.setDailyGoal(userId, request.minutes());
	}

	@PostMapping("/complete")
	public ProgressResponse complete(
			@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody CompleteWorkoutRequest request) {
		return progressService.complete(userId, request);
	}

	@GetMapping("/badges")
	public List<BadgeResponse> badges(@RequestHeader("X-User-Id") Long userId) {
		return progressService.badges(userId);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, String>> handleStatus(ResponseStatusException ex) {
		return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", String.valueOf(ex.getReason())));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.findFirst()
				.map(err -> err.getField() + " " + err.getDefaultMessage())
				.orElse("Invalid request");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", message));
	}
}
