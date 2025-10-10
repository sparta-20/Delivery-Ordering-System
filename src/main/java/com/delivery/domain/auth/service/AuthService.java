package com.delivery.domain.auth.service;

import com.delivery.domain.auth.dto.SignUpRequestDto;

public interface AuthService {
    void signup(SignUpRequestDto signUpRequestDto);
}
