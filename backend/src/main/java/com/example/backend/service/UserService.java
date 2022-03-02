package com.example.backend.service;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.entity.VerifyID;
import com.example.backend.model.VerifyForm;
import com.example.backend.repo.RoleRepo;
import com.example.backend.repo.UserRepo;
import com.example.backend.repo.VerifyIdRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private RoleRepo roleRepo;
    private UserRepo userRepo;
    private VerifyIdRepo verifyIdRepo;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("{}", username);
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("user not found in the db");
            throw new UsernameNotFoundException("user not found in the db");
        }
        if (!user.isEnabled()) {
            log.error("user not verified yet");
            throw new UsernameNotFoundException("user not verified yet");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), authorities);

    }

    public UserService(RoleRepo roleRepo, UserRepo userRepo, VerifyIdRepo verifyIdRepo,
                       PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.verifyIdRepo = verifyIdRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public User saveUser(User user) throws Exception {
        log.info("Saving new user {} to db", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepo.findByUsername(user.getUsername()) != null) {
            log.error("Username already exists, pick a new one");
            throw new Exception("Username already exists, pick a new one");
        } else {
            return userRepo.save(user);
        }
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to db", role.getName());
        return roleRepo.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepo.findByUsername(username);
        if (user != null) {
            Role role = roleRepo.findByName(roleName);
            if (role != null) {
                user.getRoles().add(role);
                return;
            }
            throw new RuntimeException("not a valid role");
        }
        throw new UsernameNotFoundException("no username provided");

    }

    public User getUser(String username) {
        log.info("Getting user {}", username);
        return userRepo.findByUsername(username);
    }

    public List<User> getUsers() {
        log.info("Getting all users");
        return userRepo.findAll();
    }

    public void sendVerificationEmail(String email, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        String verifyId = UUID.randomUUID().toString().substring(0, 5);

        message.setFrom("shiro@gmail.com");
        message.setTo("grittygerm93@gmail.com");
//        message.setTo(email);
        message.setText("Your verification pin is %s".formatted(verifyId));
        message.setSubject("Verification PIN");

        mailSender.send(message);
        log.info("mail sent, pin is {}",verifyId);

        verifyIdRepo.save(new VerifyID(verifyId, user));
    }

    public String validateId(VerifyForm verifyId) {
        log.info("verifyId is {}", verifyId.getVerifyId());
        User user = userRepo.findByEmail(verifyId.getEmail());
        VerifyID verificationID = verifyIdRepo.findByUser(user);

        if (verificationID == null)
            return "invalid credentials submitted";

//      if token expiry time is earlier than current time will return neg
        if (verificationID.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            verifyIdRepo.delete(verificationID);
            return "your pin has expired";
        }

        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }
}