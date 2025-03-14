package com.example.demo.application.domain;

import com.example.demo.common.BaseEntity;
import com.example.demo.competition.domain.Competition;
import com.example.demo.team.domain.Team;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "application_forms")
public class ApplicationForm extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "team_experience")
    private String teamExperience;

    @Column(name = "robot_specifications", columnDefinition = "TEXT")
    private String robotSpecifications;

    @Column(name = "additional_equipment", columnDefinition = "TEXT")
    private String additionalEquipment;

    @Column(name = "special_requirements")
    private String specialRequirements;

    @Column(name = "admin_comment", columnDefinition = "TEXT")
    private String adminComment;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;
}
