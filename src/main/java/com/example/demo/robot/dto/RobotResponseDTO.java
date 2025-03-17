package com.example.demo.robot.dto;

import com.example.demo.common.dto.ImageDTO;
import com.example.demo.common.dto.ImageResponseDTO;

public record RobotResponseDTO(
        Long id,
        String name,
        String description,
        ImageResponseDTO image
) {
}
