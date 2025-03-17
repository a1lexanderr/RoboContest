package com.example.demo.team.dto;

import com.example.demo.common.dto.ImageResponseDTO;
import com.example.demo.robot.dto.RobotResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record TeamResponseDTO(
        Long id,
        String name,
        String description,
        ImageResponseDTO image,
        RobotResponseDTO robot,
        List<TeamMemberResponseDTO> members,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
