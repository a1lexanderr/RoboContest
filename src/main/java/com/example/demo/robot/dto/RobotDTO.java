package com.example.demo.robot.dto;

import com.example.demo.common.dto.ImageDTO;

public record RobotDTO(
        String name,
        String description,
        ImageDTO image
) {
}
