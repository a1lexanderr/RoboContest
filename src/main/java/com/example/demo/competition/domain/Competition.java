package com.example.demo.competition.domain;

import com.example.demo.application.domain.ApplicationForm;
import com.example.demo.common.BaseEntity;
import com.example.demo.common.Image;
import com.example.demo.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "competitions")
@Builder
public class Competition extends BaseEntity {
    private String title;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
    @Enumerated(EnumType.STRING)
    private CompetitionStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @Builder.Default
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private List<ApplicationForm> applications = new ArrayList<>();
}
