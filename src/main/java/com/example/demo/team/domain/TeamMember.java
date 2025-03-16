package com.example.demo.team.domain;

import com.example.demo.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @Column(name = "team_id")
    private Team team;
}
