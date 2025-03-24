package com.example.demo.team.mapper;

import com.example.demo.team.domain.Team;
import com.example.demo.team.dto.TeamDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team toEntity(TeamDTO teamDTO);
}
