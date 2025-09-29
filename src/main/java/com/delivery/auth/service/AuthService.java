package com.delivery.auth.service;

import com.delivery.auth.dto.SignUpRequestDto;
import com.delivery.exception.BusinessException;
import com.delivery.exception.ErrorCode;
import com.delivery.user.entity.User;
import com.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignUpRequestDto signUpRequestDto) {
        if(userRepository.existsByNickname(signUpRequestDto.getNickname())){
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        User user = new User(signUpRequestDto.getNickname(), signUpRequestDto.getEmail(), encodedPassword);
        userRepository.save(user);
    }
}
