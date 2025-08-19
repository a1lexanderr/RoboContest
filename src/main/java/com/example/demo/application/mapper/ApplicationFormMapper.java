package com.example.demo.application.mapper;

import com.example.demo.application.domain.ApplicationForm;
import com.example.demo.application.dto.ApplicationFormCreateDTO;
import com.example.demo.application.dto.ApplicationFormResponseDTO;
import com.example.demo.application.dto.ApplicationFormSummaryDTO;
import com.example.demo.application.dto.ApplicationFormUpdateDTO;
import com.example.demo.competition.mapper.CompetitionMapper;
import com.example.demo.team.mapper.TeamMapper;
import com.example.demo.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TeamMapper.class, CompetitionMapper.class, UserMapper.class})
public interface ApplicationFormMapper {
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "competition", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminComment", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    ApplicationForm toEntity(ApplicationFormCreateDTO createDTO);

    @Mapping(target = "team", ignore = true)
    @Mapping(target = "competition", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminComment", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    void updateApplicationFromDto(ApplicationFormUpdateDTO updateDTO, @MappingTarget ApplicationForm application);

    @Mapping(source = "team.name", target = "teamName")
    @Mapping(source = "competition.id", target = "competitionId")
    @Mapping(source = "competition.title", target = "competitionTitle")
    @Mapping(source = "reviewedBy.username", target = "reviewedByUsername")
    ApplicationFormResponseDTO toResponseDTO(ApplicationForm applicationForm);

    @Mapping(source = "team.name", target = "teamName")
    @Mapping(source = "competition.title", target = "competitionTitle")
    ApplicationFormSummaryDTO toSummaryDTO(ApplicationForm applicationForm);
}