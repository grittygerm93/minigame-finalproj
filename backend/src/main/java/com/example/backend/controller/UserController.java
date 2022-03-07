package com.example.backend.controller;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.model.NewUser;
import com.example.backend.model.RoleToUserForm;
import com.example.backend.model.VerifyForm;
import com.example.backend.service.UserService;
import com.example.backend.util.JwtUtil;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/grit")
    public ResponseEntity<String> getRecipe() {
        JsonObject jsonObject =
                Json.createObjectBuilder().add("key", "value").build();
        return ResponseEntity.ok(jsonObject.toString());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/user/save")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/role/save")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                String username =
                        JwtUtil.getUsernameFromRefreshToken(refresh_token);

//                check if user really exists in our system
//                no verification is needed..
                User user = userService.getUser(username);
                String access_token = JwtUtil.createAccessTokenAgain(user, request);

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                        .add("access_token", access_token)
                        .add("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getOutputStream().write(objectBuilder.build().toString().getBytes());

            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                        .add("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getOutputStream().write(objectBuilder.build().toString().getBytes());
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NewUser newUser) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/register")
                .toUriString());

        User user = new User(null, newUser.getUsername(), newUser.getPassword(), newUser.getEmail(), false, new ArrayList<>(), null);
        User savedUser = null;
        try {
            savedUser = userService.saveUser(user);
        } catch (Exception e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("error_message", e.getMessage());
            return ResponseEntity.badRequest().body(objectBuilder.build().toString());
        }
        userService.addRoleToUser(user.getUsername(), "ROLE_USER");
        userService.sendVerificationEmail(user.getEmail(), user);
        return ResponseEntity.created(uri).body(savedUser);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerifyForm verifyForm) {
        String status = userService.validateId(verifyForm);
        log.info("status is {}", status);
        if (!status.equals("valid")) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add("error_message", status);
            return ResponseEntity.badRequest().body(objectBuilder.build().toString());
        }
        return ResponseEntity.ok().build();
    }

}
