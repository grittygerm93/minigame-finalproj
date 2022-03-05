package com.example.backend.service.websocket;

import com.example.backend.service.UserService;
import com.example.backend.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WebSocketAuthenticatorService {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authManager;

    public WebSocketAuthenticatorService(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(String jwt) throws AuthenticationException {

//        for the original version that doesnt use jwt
/*        if(username == null || username.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }

        // Check that the user with that username exists
        UserDetails userDetails = userService.loadUserByUsername(username);
        if(userDetails == null){

            throw new AuthenticationCredentialsNotFoundException("User not found");

        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username,
                password,
                userDetails.getAuthorities());*/

        /* UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username,
                password,
                Collections.singletonList(new SimpleGrantedAuthority(user.getAuthority()));*/

        if (jwt == null || jwt.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("jwt access token was null or empty.");
        }

//        todo handle refresh token case.. this is not used yet
//        if (jwtRefresh == null || jwtRefresh.trim().isEmpty()) {
//            throw new AuthenticationCredentialsNotFoundException("jwt refresh token was null or empty.");
//        }
        UsernamePasswordAuthenticationToken token =
                JwtUtil.createAuthorizationToken(jwt);

        SecurityContextHolder.getContext().setAuthentication(token);

        // verify that the credentials are valid
//        authManager.authenticate(token);

        // Erase the password in the token after verifying it because we will pass it to the STOMP headers.
        //        credentials already dont have pass??
        token.eraseCredentials();

        return token;
    }
}
