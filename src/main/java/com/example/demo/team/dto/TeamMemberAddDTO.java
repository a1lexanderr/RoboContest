package com.example.demo.team.dto;

import jakarta.validation.constraints.NotNull;

public record TeamMemberAddDTO(
        @NotNull(message = "Id cannot be null")
        Long userId,
        String role
) {
}
