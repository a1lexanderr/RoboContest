package com.example.demo.team.controller;

import com.example.demo.team.domain.Team;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
public class TeamController {
    public ResponseEntity<?> createTeam() {}
}
