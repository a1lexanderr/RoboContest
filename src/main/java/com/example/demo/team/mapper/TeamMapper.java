package com.example.demo.team.mapper;

import com.example.demo.common.mapper.ImageMapper;
import com.example.demo.robot.mapper.RobotMapper;
import com.example.demo.team.domain.Team;
import com.example.demo.team.dto.TeamCreateDTO;
import com.example.demo.team.dto.TeamResponseDTO;
import com.example.demo.team.dto.TeamSummaryDTO;
import com.example.demo.team.dto.TeamUpdateDTO;
import com.example.demo.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ImageMapper.class, UserMapper.class, RobotMapper.class, TeamMemberMapper.class})
public interface TeamMapper {
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "captain", ignore = true)
    @Mapping(target = "robot", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "applications", ignore = true)
    Team toEntity(TeamCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "captain", ignore = true)
    @Mapping(target = "robot", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateTeamFromDetailsDto(TeamUpdateDTO dto, @MappingTarget Team entity);

    TeamResponseDTO toResponseDTO(Team team);
    @Mapping(source = "image.url", target = "image")
    TeamSummaryDTO toSummaryDTO(Team team);
}