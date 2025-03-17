package com.example.demo.team.dto;

import com.example.demo.common.dto.ImageDTO;
import com.example.demo.robot.domain.Robot;
import com.example.demo.robot.dto.RobotDTO;
import com.example.demo.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TeamDTO(
        @NotBlank @Size(min = 3, max = 30)
        String name,
        String description,
        ImageDTO image,
        RobotDTO robot,
        List<TeamMemberDTO> members
) {
}
