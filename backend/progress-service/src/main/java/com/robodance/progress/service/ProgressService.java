package com.robodance.progress.service;

import com.robodance.progress.domain.DanceLevel;
import com.robodance.progress.dto.BadgeResponse;
import com.robodance.progress.dto.CompleteWorkoutRequest;
import com.robodance.progress.dto.ProgressResponse;
import com.robodance.progress.entity.UserBadge;
import com.robodance.progress.entity.UserProgress;
import com.robodance.progress.entity.WorkoutCompletion;
import com.robodance.progress.repo.UserBadgeRepository;
import com.robodance.progress.repo.UserProgressRepository;
import com.robodance.progress.repo.WorkoutCompletionRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProgressService {

	private static final Map<String, String[]> CATALOG = new LinkedHashMap<>();

	static {
		CATALOG.put("FIRST_STEP", new String[] {"First Step", "Complete your first workout"});
		CATALOG.put("STREAK_3", new String[] {"Circuit Streak", "Maintain a 3-day streak"});
		CATALOG.put("STREAK_7", new String[] {"Weekly Bot", "Maintain a 7-day streak"});
		CATALOG.put("CALORIES_500", new String[] {"Calorie Crusher", "Burn 500 estimated calories"});
		CATALOG.put("TEN_WORKOUTS", new String[] {"Gear Grinder", "Complete 10 workouts"});
		CATALOG.put("LEVEL_10", new String[] {"Day 10 Unlocked", "Complete 10 days in a level"});
	}

	private final UserProgressRepository progressRepository;
	private final WorkoutCompletionRepository completionRepository;
	private final UserBadgeRepository badgeRepository;

	public ProgressService(
			UserProgressRepository progressRepository,
			WorkoutCompletionRepository completionRepository,
			UserBadgeRepository badgeRepository) {
		this.progressRepository = progressRepository;
		this.completionRepository = completionRepository;
		this.badgeRepository = badgeRepository;
	}

	@Transactional
	public ProgressResponse getProgress(Long userId) {
		UserProgress progress = getOrCreate(userId);
		return toResponse(progress);
	}

	@Transactional
	public ProgressResponse setLevel(Long userId, String levelRaw) {
		UserProgress progress = getOrCreate(userId);
		progress.setActiveLevel(parseLevel(levelRaw));
		progressRepository.save(progress);
		return toResponse(progress);
	}

	@Transactional
	public ProgressResponse setDailyGoal(Long userId, int minutes) {
		if (minutes != 10 && minutes != 20 && minutes != 30) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Daily goal must be 10, 20, or 30 minutes");
		}
		UserProgress progress = getOrCreate(userId);
		progress.setDailyGoalMinutes(minutes);
		progressRepository.save(progress);
		return toResponse(progress);
	}

	@Transactional
	public ProgressResponse complete(Long userId, CompleteWorkoutRequest request) {
		DanceLevel level = parseLevel(request.level());
		UserProgress progress = getOrCreate(userId);

		if (completionRepository.existsByUserIdAndLevelAndDayNumber(userId, level, request.day())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Workout already completed");
		}

		if (request.day() > 1
				&& !completionRepository.existsByUserIdAndLevelAndDayNumber(userId, level, request.day() - 1)) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Complete day " + (request.day() - 1) + " before day " + request.day());
		}

		int calories = estimateCalories(level, request.minutes());

		WorkoutCompletion completion = new WorkoutCompletion();
		completion.setUserId(userId);
		completion.setLevel(level);
		completion.setDayNumber(request.day());
		completion.setMinutes(request.minutes());
		completion.setCalories(calories);
		completionRepository.save(completion);

		progress.setActiveLevel(level);
		progress.setTotalMinutes(progress.getTotalMinutes() + request.minutes());
		progress.setTotalCalories(progress.getTotalCalories() + calories);
		updateStreak(progress);
		progressRepository.save(progress);

		awardBadges(userId, progress);

		return toResponse(progress);
	}

	@Transactional(readOnly = true)
	public List<BadgeResponse> badges(Long userId) {
		List<UserBadge> earned = badgeRepository.findByUserIdOrderByEarnedAtDesc(userId);
		Map<String, UserBadge> earnedMap = new LinkedHashMap<>();
		for (UserBadge badge : earned) {
			earnedMap.put(badge.getBadgeCode(), badge);
		}

		List<BadgeResponse> result = new ArrayList<>();
		for (Map.Entry<String, String[]> entry : CATALOG.entrySet()) {
			UserBadge badge = earnedMap.get(entry.getKey());
			result.add(new BadgeResponse(
					entry.getKey(),
					entry.getValue()[0],
					entry.getValue()[1],
					badge != null,
					badge == null ? null : badge.getEarnedAt().toString()));
		}
		return result;
	}

	private void awardBadges(Long userId, UserProgress progress) {
		long completed = completionRepository.countByUserId(userId);
		long levelCount = completionRepository.countByUserIdAndLevel(userId, progress.getActiveLevel());

		if (completed >= 1) {
			grant(userId, "FIRST_STEP");
		}
		if (progress.getCurrentStreak() >= 3) {
			grant(userId, "STREAK_3");
		}
		if (progress.getCurrentStreak() >= 7) {
			grant(userId, "STREAK_7");
		}
		if (progress.getTotalCalories() >= 500) {
			grant(userId, "CALORIES_500");
		}
		if (completed >= 10) {
			grant(userId, "TEN_WORKOUTS");
		}
		if (levelCount >= 10) {
			grant(userId, "LEVEL_10");
		}
	}

	private void grant(Long userId, String code) {
		if (badgeRepository.existsByUserIdAndBadgeCode(userId, code)) {
			return;
		}
		String[] meta = CATALOG.get(code);
		UserBadge badge = new UserBadge();
		badge.setUserId(userId);
		badge.setBadgeCode(code);
		badge.setTitle(meta[0]);
		badge.setDescription(meta[1]);
		badgeRepository.save(badge);
	}

	private void updateStreak(UserProgress progress) {
		LocalDate today = LocalDate.now();
		LocalDate last = progress.getLastWorkoutDate();
		if (last == null) {
			progress.setCurrentStreak(1);
		} else if (last.equals(today)) {
			// same day: keep streak
		} else if (ChronoUnit.DAYS.between(last, today) == 1) {
			progress.setCurrentStreak(progress.getCurrentStreak() + 1);
		} else {
			progress.setCurrentStreak(1);
		}
		progress.setLastWorkoutDate(today);
		if (progress.getCurrentStreak() > progress.getLongestStreak()) {
			progress.setLongestStreak(progress.getCurrentStreak());
		}
	}

	private UserProgress getOrCreate(Long userId) {
		return progressRepository.findById(userId).orElseGet(() -> {
			UserProgress created = new UserProgress();
			created.setUserId(userId);
			return progressRepository.save(created);
		});
	}

	private ProgressResponse toResponse(UserProgress progress) {
		List<ProgressResponse.HistoryItem> history = completionRepository
				.findByUserIdOrderByCompletedAtDesc(progress.getUserId())
				.stream()
				.map(item -> new ProgressResponse.HistoryItem(
						item.getLevel().name(),
						item.getDayNumber(),
						item.getMinutes(),
						item.getCalories(),
						item.getCompletedAt().toString()))
				.toList();

		int nextDay = 1;
		while (nextDay <= 30
				&& completionRepository.existsByUserIdAndLevelAndDayNumber(
						progress.getUserId(), progress.getActiveLevel(), nextDay)) {
			nextDay++;
		}

		return new ProgressResponse(
				progress.getActiveLevel().name(),
				progress.getDailyGoalMinutes(),
				progress.getCurrentStreak(),
				progress.getLongestStreak(),
				progress.getTotalCalories(),
				progress.getTotalMinutes(),
				Math.min(nextDay, 30),
				history);
	}

	private int estimateCalories(DanceLevel level, int minutes) {
		int perMinute = switch (level) {
			case BEGINNER -> 5;
			case INTERMEDIATE -> 6;
			case ADVANCED -> 8;
		};
		return minutes * perMinute;
	}

	public static DanceLevel parseLevel(String level) {
		try {
			return DanceLevel.valueOf(level.trim().toUpperCase());
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid level");
		}
	}
}
