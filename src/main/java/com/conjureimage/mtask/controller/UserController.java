package com.conjureimage.mtask.controller;

import com.conjureimage.mtask.domain.AppUser;
import com.conjureimage.mtask.exception.UserNotAuthenticated;
import com.conjureimage.mtask.repository.AppUserRepository;
import com.conjureimage.mtask.security.utils.SecurityUtil;
import com.conjureimage.mtask.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController

public class UserController {
    AppUserRepository appUserRepository;

    @GetMapping("/api/me")
    public AppUser me(HttpServletRequest request) throws UserNotAuthenticated {
        return SecurityUtil.getUserDetails(request);
    }

    @GetMapping("/api/users")
    public Collection<AppUser> searchUsers(@RequestParam(value = "query") String query) {
        if (query == null) {
            return new ArrayList<>();
        }
        String transposedQuery = StringUtils.transpose(query);
        List<AppUser> a = appUserRepository.findUsers(query);
        List<AppUser> b = appUserRepository.findUsers(transposedQuery);
        Set<AppUser> all = new HashSet<>();
        all.addAll(a);
        all.addAll(b);
        return all;
    }
}
