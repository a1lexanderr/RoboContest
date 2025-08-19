package com.example.demo.team.dto;

import jakarta.validation.constraints.NotNull;

public record TeamMemberAddDTO(
        String username,
        String role
) {
}
