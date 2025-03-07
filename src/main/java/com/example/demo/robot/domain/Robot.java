package com.example.demo.robot.domain;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.Image;
import com.example.demo.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "robot")
public class Robot extends BaseEntity {
    private String name;
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToOne(mappedBy = "robot")
    private Team team;
}
