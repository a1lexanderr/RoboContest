package com.example.demo.team.service;

import com.example.demo.common.exception.business.user.UserNotFoundException;
import com.example.demo.team.domain.Team;
import com.example.demo.team.dto.TeamDTO;
import com.example.demo.team.dto.TeamResponseDTO;
import com.example.demo.team.repository.TeamRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamServiceImpl(final TeamRepository teamRepository, final UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }


    @Override
    public TeamResponseDTO createTeam(TeamDTO teamDTO, Long id) {
        User captain = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException(id));

        Team team = Team.builder()
                .name(teamDTO.name())
                .description(teamDTO.description())
                .build();

        
        return null;
    }
}
