package com.conjureimage.mtask.service;

import com.conjureimage.mtask.config.JwtCoder;
import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.dtos.LoginDto;
import com.conjureimage.mtask.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;

    public String register (AppUser user) {
        if (userRepository.findByEmail(user.getEmail()) != null) return null;

        user = userRepository.save(user);
        return JwtCoder.generateJwt(user);
    }


    public String login (LoginDto dto) {
        AppUser user = userRepository.findByEmail(dto.getLogin());
        if (user == null || !Objects.equals(user.getPassword(), dto.getPassword())) {
            return null;
        }
        return JwtCoder.generateJwt(user);
    }
}
