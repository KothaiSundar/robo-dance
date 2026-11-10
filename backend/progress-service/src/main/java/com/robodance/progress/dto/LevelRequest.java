package com.robodance.progress.dto;

import jakarta.validation.constraints.NotBlank;

public record LevelRequest(@NotBlank String level) {
}
