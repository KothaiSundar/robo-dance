package com.robodance.workouts.config;

import com.robodance.workouts.domain.DanceLevel;
import com.robodance.workouts.entity.DanceStep;
import com.robodance.workouts.entity.Workout;
import com.robodance.workouts.repo.WorkoutRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WorkoutSeeder implements CommandLineRunner {

	private static final String VIDEO_URL =
			"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

	private static final String[] BEGINNER_MOVES = {
			"Robot Arms", "Servo Side Step", "Moonwalk Glide", "Freeze Pose",
			"Gear Turn", "Boot Stomp", "Antenna Wave", "Circuit Clap"
	};

	private static final String[] INTERMEDIATE_MOVES = {
			"Glitch Pop", "Circuit Wave", "Hydraulic Drop", "Beat Lock Combo",
			"Pixel Spin", "Voltage Kick", "Servo Shuffle", "Neon Punch"
	};

	private static final String[] ADVANCED_MOVES = {
			"Bot Battle Combo", "Turbo Isolation", "Power Down Finale", "Overclock Spin",
			"Laser Slide", "Quantum Lock", "Voltage Storm", "Core Meltdown"
	};

	private final WorkoutRepository workoutRepository;

	public WorkoutSeeder(WorkoutRepository workoutRepository) {
		this.workoutRepository = workoutRepository;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (workoutRepository.count() > 0) {
			return;
		}

		for (DanceLevel level : DanceLevel.values()) {
			for (int day = 1; day <= 30; day++) {
				workoutRepository.save(buildWorkout(level, day));
			}
		}
	}

	private Workout buildWorkout(DanceLevel level, int day) {
		String[] moves = movesFor(level);
		int duration = switch (level) {
			case BEGINNER -> 10 + ((day - 1) % 3) * 10; // 10/20/30 rotating
			case INTERMEDIATE -> 20 + ((day - 1) % 2) * 10; // 20/30
			case ADVANCED -> 30;
		};

		Workout workout = new Workout();
		workout.setLevel(level);
		workout.setDayNumber(day);
		workout.setDurationMinutes(duration);
		workout.setVideoUrl(VIDEO_URL);
		workout.setTitle(level.name().charAt(0) + level.name().substring(1).toLowerCase()
				+ " Day " + day + ": " + moves[(day - 1) % moves.length] + " Session");
		workout.setDescription("Robo Dance " + level.name().toLowerCase()
				+ " routine for day " + day + ". Follow the step list, then mark complete.");

		String[] howTos = {
				"Keep elbows rigid and pulse on the beat.",
				"Slide feet in short servo-like steps.",
				"Hold the freeze for a full 8-count.",
				"Drop and recover with mechanical control.",
				"Isolate chest and shoulders like a glitch loop.",
				"Finish clean — power-down pose on the last beat."
		};

		// Warm-up + 3 main moves + cool-down = 5 steps, unique per day via offsets
		String[] stepNames = {
				"Warm-up " + moves[day % moves.length],
				moves[(day) % moves.length],
				moves[(day + 2) % moves.length],
				moves[(day + 4) % moves.length],
				"Cool-down Freeze " + (day % 8 + 1)
		};

		for (int i = 0; i < stepNames.length; i++) {
			DanceStep step = new DanceStep();
			step.setStepOrder(i + 1);
			step.setName(stepNames[i]);
			step.setCounts((i == 0 || i == 4) ? "4x8" : "8x8");
			step.setInstruction(howTos[(day + i) % howTos.length]);
			workout.addStep(step);
		}

		return workout;
	}

	private String[] movesFor(DanceLevel level) {
		return switch (level) {
			case BEGINNER -> BEGINNER_MOVES;
			case INTERMEDIATE -> INTERMEDIATE_MOVES;
			case ADVANCED -> ADVANCED_MOVES;
		};
	}
}
