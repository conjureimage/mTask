package com.conjureimage.mtask.controller;

import com.conjureimage.mtask.config.JwtCoder;
import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.dtos.LoginDto;
import com.conjureimage.mtask.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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

    @GetMapping(value = "/info")
        public String getName(HttpServletRequest request) {
        Claims claims = JwtCoder.decodeJwt(request.getHeader("Authorization"));
        return claims.getSubject();
    }
}
