package com.example.backend.util.oauth;

import com.example.backend.entity.User;
import com.example.backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtil {

    @Autowired
    private UserRepo userRepo;
    private User user;

    //todo change this...
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo.findByUsername((String)authentication.getPrincipal());
//        User currentUser = userRepo.findByUsername(username);
//        user = currentUser;

        return currentUser;
    }

//    public User getCurrentUser() {
//        return user;
//    }


//    public boolean authorized(String expectedId) {
//        boolean auth = false;
//
//        User currentUser = getCurrentUser();
//
//        if (currentUser.getId().equals(expectedId)) {
//            auth = true;
//        }
//
//        return auth;
//    }
}
