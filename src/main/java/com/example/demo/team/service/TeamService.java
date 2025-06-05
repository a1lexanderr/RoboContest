package com.example.demo.team.service;

import com.example.demo.team.dto.*;
import com.example.demo.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface TeamService {

    TeamResponseDTO createTeam(TeamCreateDTO teamCreateDTO, MultipartFile imageFile, User captain);
    TeamResponseDTO getTeamById(Long teamId);
    TeamResponseDTO updateTeamDetails(Long teamId, TeamUpdateDTO updateDTO, User currentUser);
    TeamResponseDTO updateTeamImage(Long teamId, MultipartFile imageFile, User currentUser);

    void deleteTeam(Long teamId, User currentUser);
    TeamMemberResponseDTO addMemberToTeam(Long teamId, TeamMemberAddDTO memberAddDTO, User currentUser);

    void removeMemberFromTeam(Long teamId, Long userIdToRemove, User currentUser);

    List<TeamMemberResponseDTO> getTeamMembers(Long teamId);

    List<TeamSummaryDTO> getTeamsForUser(User user);

    Page<TeamSummaryDTO> getAllTeams(String searchQuery, Pageable pageable);
}