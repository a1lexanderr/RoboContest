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
    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
    @Enumerated(EnumType.STRING)
    private CompetitionStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ApplicationForm> applications = new ArrayList<>();
}
