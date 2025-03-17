package com.example.demo.common.dto;

public record ImageResponseDTO(
        Long id,
        String title,
        boolean isMain,
        String url
) {}
