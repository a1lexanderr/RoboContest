package com.example.demo.robot.service;

import com.example.demo.common.Image;
import com.example.demo.common.exception.business.robot.RobotNotFoundException;
import com.example.demo.common.exception.business.team.TeamNotFoundException;
import com.example.demo.common.service.ImageService;
import com.example.demo.robot.domain.Robot;
import com.example.demo.robot.dto.RobotDTO;
import com.example.demo.robot.dto.RobotResponseDTO;
import com.example.demo.robot.mapper.RobotMapper;
import com.example.demo.robot.repository.RobotRepository;
import com.example.demo.team.domain.Team;
import com.example.demo.team.repository.TeamRepository;
import com.example.demo.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class RobotServiceImpl implements RobotService {
    private final TeamRepository teamRepository;
    private final RobotRepository robotRepository;
    private final ImageService imageService;
    private final RobotMapper robotMapper;


    public RobotServiceImpl(
            TeamRepository teamRepository,
            RobotRepository robotRepository,
            RobotMapper robotMapper,
            ImageService imageService) {
        this.teamRepository = teamRepository;
        this.robotRepository = robotRepository;
        this.robotMapper = robotMapper;
        this.imageService = imageService;
    }

    @Override
    @Transactional
    public RobotResponseDTO createRobotForTeam(Long teamId, RobotDTO robotDTO, MultipartFile imageFile, User currentUser) {
        log.info("Попытка создать робота для команды ID: {} пользователем: {}", teamId, currentUser.getUsername());

        Team team = findAndAuthorizeTeamOrThrow(teamId, currentUser);

        if (team.getRobot() != null) {
            log.warn("У команды ID: {} уже есть робот. Создание нового робота отменено.", teamId);
            throw new IllegalStateException("Команда " + teamId + " уже имеет робота. Для изменения используйте обновление.");
        }

        Robot robot = robotMapper.toEntity(robotDTO);
        robot.setTeam(team);

        processRobotImage(robot, imageFile, null);

        Robot savedRobot = robotRepository.save(robot);

        team.setRobot(savedRobot);
        teamRepository.save(team);

        log.info("Робот ID: {} успешно создан и связан с командой ID: {}", savedRobot.getId(), teamId);
        return robotMapper.toResponseDTO(savedRobot);
    }

    @Override
    @Transactional
    public RobotResponseDTO updateRobotForTeam(Long teamId, RobotDTO robotDTO, MultipartFile imageFile, User currentUser) {
        log.info("Попытка обновить робота для команды ID: {} пользователем: {}", teamId, currentUser.getUsername());

        Team team = findAndAuthorizeTeamOrThrow(teamId, currentUser);

        Robot robot = team.getRobot();
        if (robot == null) {
            log.warn("Робот для команды ID: {} не найден. Обновление невозможно.", teamId);
            throw new RobotNotFoundException("Робот для команды " + teamId + " не найден.");
        }

        Image oldImage = robot.getImage();
        robotMapper.updateRobotFromDto(robotDTO, robot);

        processRobotImage(robot, imageFile, oldImage);

        Robot savedRobot = robotRepository.save(robot);

        log.info("Робот ID: {} успешно обновлен для команды ID: {}", savedRobot.getId(), teamId);
        return robotMapper.toResponseDTO(savedRobot);
    }

    @Override
    @Transactional(readOnly = true)
    public RobotResponseDTO findRobotByTeamId(Long teamId) {
        log.debug("Поиск робота для команды ID: {}", teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Команда с ID " + teamId + " не найдена."));

        if (team.getRobot() == null) {
            log.info("У команды ID: {} нет назначенного робота.", teamId);
            throw new RobotNotFoundException("Робот для команды с ID " + teamId + " не найден.");
        }
        return robotMapper.toResponseDTO(team.getRobot());
    }

    @Override
    @Transactional(readOnly = true)
    public RobotResponseDTO findRobotById(Long robotId) {
        log.debug("Поиск робота по ID: {}", robotId);
        Robot robot = robotRepository.findById(robotId)
                .orElseThrow(() -> new RobotNotFoundException("Робот с ID " + robotId + " не найден."));
        return robotMapper.toResponseDTO(robot);
    }

    @Override
    @Transactional
    public void deleteRobotForTeam(Long teamId, User currentUser) {
        log.info("Попытка удалить робота для команды ID: {} пользователем: {}", teamId, currentUser.getUsername());
        Team team = findAndAuthorizeTeamOrThrow(teamId, currentUser);

        Robot robot = team.getRobot();
        if (robot == null) {
            log.info("У команды ID: {} нет робота для удаления.", teamId);
            return;
        }
        Long robotIdToDelete = robot.getId();
        Image imageToDelete = robot.getImage();

        team.setRobot(null);
        teamRepository.save(team);
        log.debug("Робот отвязан от команды ID: {}", teamId);

        if (imageToDelete != null) {
            try {
                imageService.deleteImage(imageToDelete.getId());
                log.info("Изображение робота (Image ID: {}) удалено.", imageToDelete.getId());
            } catch (Exception e) {
                log.error("Не удалось удалить изображение робота (Image ID: {}): {}", imageToDelete.getId(), e.getMessage());
            }
        }

        robotRepository.deleteById(robotIdToDelete);
        log.info("Робот ID: {} успешно удален.", robotIdToDelete);
    }

    private void processRobotImage(Robot robot, MultipartFile imageFile, Image oldImageToDelete) {
        if (imageFile != null && !imageFile.isEmpty()) {
            log.debug("Обработка нового файла изображения для робота.");
            if (oldImageToDelete != null) {
                log.debug("Удаление старого изображения робота ID: {}", oldImageToDelete.getId());
                try {
                    imageService.deleteImage(oldImageToDelete.getId());
                } catch (Exception e) {
                    log.error("Не удалось удалить старое изображение робота (Image ID: {}): {}", oldImageToDelete.getId(), e.getMessage());
                }
            }
            String imageTitle = (robot.getName() != null ? robot.getName() : "Robot") + " Profile Image";
            Image newRobotImage = imageService.saveImage(imageFile, imageTitle, false, "robot_profile");
            robot.setImage(newRobotImage);
            log.info("Новое изображение ID: {} сохранено для робота.", newRobotImage.getId());
        }
    }

    private Team findAndAuthorizeTeamOrThrow(Long teamId, User currentUser) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> {
                    log.warn("Команда с ID: {} не найдена.", teamId);
                    return new TeamNotFoundException("Команда с ID " + teamId + " не найдена.");
                });

        if (!team.getCaptain().getId().equals(currentUser.getId())) {
            log.warn("Пользователь {} (ID: {}) попытался изменить робота команды ID: {}, не будучи капитаном.",
                    currentUser.getUsername(), currentUser.getId(), teamId);
            throw new AccessDeniedException("Только капитан команды может настраивать робота.");
        }
        return team;
    }
}
