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
    private static AppUserRepository appUserRepository = null;

    public SecurityUtil(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }


    public static AppUser getUserDetails() {
        return appUserRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
