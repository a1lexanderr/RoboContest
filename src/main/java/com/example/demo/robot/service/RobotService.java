package com.example.demo.robot.service;

import com.example.demo.robot.domain.Robot;
import com.example.demo.robot.dto.RobotDTO;
import com.example.demo.robot.dto.RobotResponseDTO;
import com.example.demo.team.repository.TeamRepository;
import com.example.demo.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface RobotService {
    RobotResponseDTO createRobotForTeam(Long teamId, RobotDTO robotDTO, MultipartFile imageFile, User currentUser);
    RobotResponseDTO updateRobotForTeam(Long teamId, RobotDTO robotDTO, MultipartFile imageFile, User currentUser);
    RobotResponseDTO findRobotByTeamId(Long teamId);
    RobotResponseDTO findRobotById(Long robotId);
    void deleteRobotForTeam(Long teamId, User currentUser);
}
