package com.example.demo.team.controller;


import com.example.demo.team.dto.TeamResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
public class TeamController {
    public ResponseEntity<TeamResponseDTO> createTeam() {

    }
}
