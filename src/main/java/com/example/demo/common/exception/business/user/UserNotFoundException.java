package com.example.demo.common.exception.business.user;

import com.example.demo.common.exception.business.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(Long id) {
        super(
                String.format("User not found: %s", id),
                HttpStatus.NOT_FOUND,
                Collections.singletonMap("id", String.valueOf(id))
        );
    }
}
