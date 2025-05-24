package com.example.demo.team.domain;

import com.example.demo.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "team_members")
public class TeamMember extends BaseEntity {
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
