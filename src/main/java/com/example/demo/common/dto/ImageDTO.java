package com.example.demo.common.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageDTO(
        Long id,
        String title,
        String url
) {
}
