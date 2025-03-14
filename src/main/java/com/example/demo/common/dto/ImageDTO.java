package com.example.demo.common.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageDTO(
        MultipartFile file,
        String title,
        boolean isMain
) {
}
