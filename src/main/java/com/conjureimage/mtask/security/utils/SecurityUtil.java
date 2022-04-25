package com.conjureimage.mtask.security.utils;

import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.exception.UserNotAuthenticated;
import com.conjureimage.mtask.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SecurityUtil {
    private static AppUserRepository appUserRepository;

    private AppUserRepository aur;

    public static void logInUser(AppUser appUser) {
        AppUser userDetails = AppUser.getBuilder()
                .id(appUser.getId())
                .password(appUser.getPassword())
                .role(appUser.getRole())
                .username(appUser.getEmail())
                .email(appUser.getEmail())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(appUser, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static AppUser getUserDetails() throws UserNotAuthenticated {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUser) {
            AppUser appUser = (AppUser) principal;

            String userEmail = appUser.getEmail();
            return appUserRepository.findByEmail(userEmail);
        } else {
            throw new UserNotAuthenticated();
        }
    }

    @PostConstruct
    public void init() {
        appUserRepository = aur;
    }
}
