package com.conjureimage.mtask.controller;

import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.dtos.LoginDto;
import com.conjureimage.mtask.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public String register (@RequestBody AppUser appUser) {
        return authService.register(appUser);
    }

    @PostMapping("/login")
    public String login (@RequestBody LoginDto dto) {
        return authService.login(dto);
    }
}
