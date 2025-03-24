package com.example.demo.robot.dto;

public record RobotResponseDTO(
        Long id,
        String name,
        String description,
        ImageResponseDTO image
) {
}
