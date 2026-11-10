package com.robodance.workouts.repo;

import com.robodance.workouts.domain.DanceLevel;
import com.robodance.workouts.entity.Workout;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

	List<Workout> findByLevelOrderByDayNumberAsc(DanceLevel level);

	Optional<Workout> findByLevelAndDayNumber(DanceLevel level, int dayNumber);
}
