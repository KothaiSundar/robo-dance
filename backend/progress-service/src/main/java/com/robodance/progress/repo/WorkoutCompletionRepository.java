package com.robodance.progress.repo;

import com.robodance.progress.domain.DanceLevel;
import com.robodance.progress.entity.WorkoutCompletion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutCompletionRepository extends JpaRepository<WorkoutCompletion, Long> {

	List<WorkoutCompletion> findByUserIdOrderByCompletedAtDesc(Long userId);

	boolean existsByUserIdAndLevelAndDayNumber(Long userId, DanceLevel level, int dayNumber);

	long countByUserId(Long userId);

	long countByUserIdAndLevel(Long userId, DanceLevel level);
}
