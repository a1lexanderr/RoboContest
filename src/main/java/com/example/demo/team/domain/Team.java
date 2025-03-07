package com.example.demo.team.domain;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.Image;
import com.example.demo.competition.domain.Competition;
import com.example.demo.robot.domain.Robot;
import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "teams")
public class Team extends BaseEntity {
    private String name;
    private String description;
    private String teamMembers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "robot_id", referencedColumnName = "id")
    private Robot robot;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "competition_id", referencedColumnName = "id")
    private Competition competition;
}
