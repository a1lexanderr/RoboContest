package com.example.demo.team.service;

import com.example.demo.common.Image;
import com.example.demo.common.exception.business.ResourceAlreadyExistsException;
import com.example.demo.common.exception.business.ResourceNotFoundException;
import com.example.demo.common.exception.business.team.TeamNotFoundException;
import com.example.demo.common.exception.business.user.UserNotFoundException;
import com.example.demo.common.service.ImageService;
import com.example.demo.robot.service.RobotService;
import com.example.demo.team.domain.Team;
import com.example.demo.team.domain.TeamMember;
import com.example.demo.team.dto.*;
import com.example.demo.team.mapper.TeamMapper;
import com.example.demo.team.mapper.TeamMemberMapper;
import com.example.demo.team.repository.TeamMemberRepository;
import com.example.demo.team.repository.TeamRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final TeamMapper teamMapper;
    private final TeamMemberMapper teamMemberMapper;
    private final RobotService robotService;

    public TeamServiceImpl(
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            UserRepository userRepository,
            ImageService imageService,
            TeamMapper teamMapper,
            TeamMemberMapper teamMemberMapper,
            RobotService robotService
            ){
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.teamMapper = teamMapper;
        this.teamMemberMapper = teamMemberMapper;
        this.robotService = robotService;
    }

    @Override
    @Transactional
    public TeamResponseDTO createTeam(TeamCreateDTO teamCreateDTO, MultipartFile imageFile, User captain) {
        log.info("Пользователь {} создает команду с названием: {}", captain.getUsername(), teamCreateDTO.name());

        if (teamRepository.existsByName(teamCreateDTO.name())) {
            throw new ResourceAlreadyExistsException("Команда с названием '" + teamCreateDTO.name() + "' уже существует.");
        }

        Team team = teamMapper.toEntity(teamCreateDTO);
        team.setCaptain(captain);

        if (imageFile != null && !imageFile.isEmpty()) {
            Image teamImage = imageService.saveImage(imageFile, team.getName() + " Logo", true, "team_logos");
            team.setImage(teamImage);
        }

        Team savedTeam = teamRepository.save(team);

        TeamMember captainAsMember = TeamMember.builder()
                .user(captain)
                .team(savedTeam)
                .role("Капитан")
                .build();
        teamMemberRepository.save(captainAsMember);
        log.info("Капитан {} добавлен как участник в команду {}", captain.getUsername(), savedTeam.getName());

        return teamMapper.toResponseDTO(savedTeam);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponseDTO getTeamById(Long teamId) {
        log.debug("Поиск команды по ID: {}", teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Команда с ID " + teamId + " не найдена."));
        return teamMapper.toResponseDTO(team);
    }

    @Override
    @Transactional
    public TeamResponseDTO updateTeamDetails(Long teamId, TeamUpdateDTO updateDTO, User currentUser) {
        log.info("Пользователь {} обновляет детали команды ID: {}", currentUser.getUsername(), teamId);
        Team team = findTeamAndAuthorizeCaptain(teamId, currentUser.getId());

        teamMapper.updateTeamFromDetailsDto(updateDTO, team);
        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toResponseDTO(updatedTeam);
    }

    @Override
    @Transactional
    public TeamResponseDTO updateTeamImage(Long teamId, MultipartFile imageFile, User currentUser) {
        log.info("Пользователь {} обновляет изображение команды ID: {}", currentUser.getUsername(), teamId);
        Team team = findTeamAndAuthorizeCaptain(teamId, currentUser.getId());

        Image oldImage = team.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            if (oldImage != null) {
                try {
                    imageService.deleteImage(oldImage.getId());
                } catch (Exception e) {
                    log.error("Не удалось удалить старое изображение команды (Image ID: {}): {}", oldImage.getId(), e.getMessage());
                }
            }
            Image newTeamImage = imageService.saveImage(imageFile, team.getName() + " Logo", true, "team_logos");
            team.setImage(newTeamImage);
        } else {
        }

        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toResponseDTO(updatedTeam);
    }

    @Override
    @Transactional
    public void deleteTeam(Long teamId, User currentUser) {
        log.info("Пользователь {} удаляет команду ID: {}", currentUser.getUsername(), teamId);
        Team team = findTeamAndAuthorizeCaptain(teamId, currentUser.getId());

        if (team.getRobot() != null) {
            try {
                robotService.deleteRobotForTeam(teamId, currentUser);
                log.info("Робот команды ID {} удален.", teamId);
            } catch (Exception e) {
                log.error("Ошибка при удалении робота для команды ID {}: {}", teamId, e.getMessage());
            }
        }

        if (team.getImage() != null) {
            try {
                imageService.deleteImage(team.getImage().getId());
                log.info("Изображение команды ID {} удалено.", teamId);
            } catch (Exception e) {
                log.error("Ошибка при удалении изображения для команды ID {}: {}", teamId, e.getMessage());
            }
        }

        teamRepository.delete(team);
        log.info("Команда ID {} успешно удалена.", teamId);
    }

    @Override
    @Transactional
    public TeamMemberResponseDTO addMemberToTeam(Long teamId, TeamMemberAddDTO memberAddDTO, User currentUser) {
        log.info("Пользователь {} добавляет участника ID {} в команду ID {}", currentUser.getUsername(), memberAddDTO.userId(), teamId);
        Team team = findTeamAndAuthorizeCaptain(teamId, currentUser.getId());

        User userToAdd = userRepository.findById(memberAddDTO.userId())
                .orElseThrow(() -> new UserNotFoundException(memberAddDTO.userId()));

        boolean alreadyMember = teamMemberRepository.existsByTeamIdAndUserId(teamId, userToAdd.getId());
        if (alreadyMember) {
            throw new ResourceAlreadyExistsException("Пользователь " + userToAdd.getUsername() + " уже является участником команды " + team.getName());
        }

        TeamMember newMember = TeamMember.builder()
                .user(userToAdd)
                .team(team)
                .role(memberAddDTO.role())
                .build();
        TeamMember savedMember = teamMemberRepository.save(newMember);

        return teamMemberMapper.toTeamMemberResponseDTO(savedMember);
    }

    @Override
    @Transactional
    public void removeMemberFromTeam(Long teamId, Long userIdToRemove, User currentUser) {
        log.info("Пользователь {} удаляет участника ID {} из команды ID {}", currentUser.getUsername(), userIdToRemove, teamId);
        Team team = findTeamAndAuthorizeCaptain(teamId, currentUser.getId());

        User memberUser = userRepository.findById(userIdToRemove)
                .orElseThrow(() -> new UserNotFoundException(userIdToRemove));

        if (team.getCaptain().getId().equals(userIdToRemove)) {
            throw new AccessDeniedException("Капитан не может быть удален из команды этим способом. Рассмотрите смену капитана или удаление команды.");
        }

        TeamMember memberToRemove = teamMemberRepository.findByTeamIdAndUserId(teamId, userIdToRemove)
                .orElseThrow(() -> new ResourceNotFoundException("Участник " + memberUser.getUsername() + " не найден в команде " + team.getName()));

        teamMemberRepository.delete(memberToRemove);
        log.info("Участник {} удален из команды {}", memberUser.getUsername(), team.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamMemberResponseDTO> getTeamMembers(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new TeamNotFoundException("Команда с ID " + teamId + " не найдена.");
        }
        List<TeamMember> members = teamMemberRepository.findByTeamId(teamId);
        return members.stream()
                .map(teamMemberMapper::toTeamMemberResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamSummaryDTO> getTeamsForUser(User user) {
        List<Team> teams = teamRepository.findDistinctByCaptainOrMembersUser(user, user);
        return teams.stream()
                .map(teamMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamSummaryDTO> getAllTeams(String searchQuery, Pageable pageable) {
        Page<Team> teamsPage;
        if (StringUtils.hasText(searchQuery)) {
            teamsPage = teamRepository.findByNameContainingIgnoreCase(searchQuery, pageable);
        } else {
            teamsPage = teamRepository.findAll(pageable);
        }
        return teamsPage.map(teamMapper::toSummaryDTO);
    }

    private Team findTeamAndAuthorizeCaptain(Long teamId, Long captainUserId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Команда с ID " + teamId + " не найдена."));
        if (!team.getCaptain().getId().equals(captainUserId)) {
            throw new AccessDeniedException("Доступ запрещен. Только капитан команды может выполнять это действие.");
        }
        return team;
    }
}
