package com.example.backend;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner run (UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(null, "pipi-lim", "pipi", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "haihai-lim", "haihai", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "jinro-lim", "jinro", "1234", new ArrayList<>()));

            userService.addRoleToUser("pipi", "ROLE_USER");
            userService.addRoleToUser("pipi", "ROLE_ADMIN");
            userService.addRoleToUser("haihai", "ROLE_MANAGER");
            userService.addRoleToUser("jinro", "ROLE_SUPER_ADMIN");
        };
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
