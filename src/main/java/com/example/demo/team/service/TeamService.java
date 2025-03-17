package com.example.demo.team.service;

import com.example.demo.team.dto.TeamDTO;
import com.example.demo.team.dto.TeamResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface TeamService {
    TeamResponseDTO createTeam(TeamDTO teamDTO, Long id);
}
