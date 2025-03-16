package com.example.demo.team.domain;

import com.example.demo.application.domain.ApplicationForm;
import com.example.demo.common.BaseEntity;
import com.example.demo.common.Image;
import com.example.demo.competition.domain.Competition;
import com.example.demo.robot.domain.Robot;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "teams")
public class Team extends BaseEntity {
    @Column(nullable = false)
    @NotBlank
    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "robot_id", referencedColumnName = "id")
    private Robot robot;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "competition_id", referencedColumnName = "id")
    private Competition competition;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<ApplicationForm> applications = new ArrayList<>();
}
