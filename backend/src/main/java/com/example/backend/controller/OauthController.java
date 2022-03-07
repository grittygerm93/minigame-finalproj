package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.model.oauth.Email;
import com.example.backend.model.oauth.GoogleAuthResponse;
import com.example.backend.model.oauth.SimplePerson;
import com.example.backend.service.oauth.GPeopleService;
import com.example.backend.service.oauth.GmailService;
import com.example.backend.util.oauth.AuthorizationUtil;
import com.example.backend.util.oauth.GoogleOauthUtil;
import com.google.api.client.auth.oauth2.Credential;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class OauthController {
//    @Autowired
//    private GmailUtil gmailUtil;

    @Autowired
    private GoogleOauthUtil googleOauthUtil;

    @Autowired
    private AuthorizationUtil authUtil;

    @Autowired
    private GmailService gmailService;

    @Autowired
    private GPeopleService gPeopleService;

    //request from to access sends back a page for user to grant permission
//    frontend shows the page and sends user to the redirecturl afterwards
    @GetMapping(value = "/authorizationCode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuthorizationCodeUrl(@RequestParam("redirectUrl") String redirectUrl) {
        log.info(redirectUrl);
        User currentUser = authUtil.getCurrentUser();

        String url = googleOauthUtil.getAuthorizationCodeUrl(currentUser.getId(), redirectUrl);
        JsonObjectBuilder link = Json.createObjectBuilder().add("link", url);

        return ResponseEntity.ok(link.build().toString());
    }

    //    after user presses to allow permission
    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody GoogleAuthResponse auth) {
        log.debug("In createToken " + auth);

        User currentUser = authUtil.getCurrentUser();

        try {
            Credential credential = googleOauthUtil.getCredentialFromCode(auth, currentUser.getId());
            JsonObjectBuilder add = Json.createObjectBuilder().add("accessToken", credential.getAccessToken());
            return ResponseEntity.ok(add.build().toString());
        } catch (Exception e) {
            log.error("Problem retrieving credential!", e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem creating token!");
    }

    @PostMapping("/messages")
    public ResponseEntity<?> sendEmailMessage(@RequestBody String emailTo) {
        Email email = new Email();
        email.setPlainText("join me to play this game");
        email.setSubject("Email subject");
//        email.setFrom("grittygerm@gmail.com");
//        todo to modify the to based on the contact chosen
        email.setTo(emailTo);

        log.info("Sending message " + emailTo);

        try {
            User currentUser = authUtil.getCurrentUser();
            Credential credential = googleOauthUtil.getCredential(currentUser.getId().toString());

            gmailService.sendEmail(email, credential);

            return ResponseEntity.ok().build();
        } catch (IOException ie) {
            log.error("Problem retrieving email!", ie);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ie.getMessage());
        } catch (GeneralSecurityException ge) {
            log.error("Security issue!", ge);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ge.getMessage());
        } catch (MessagingException me) {
            log.error("Messaging issue!", me);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(me.getMessage());
        }
    }

    @GetMapping("/contacts")
    public ResponseEntity<?> contacts() {
        log.info("in contacts");

        User currentUser = authUtil.getCurrentUser();
        Credential credential = googleOauthUtil.getCredential(currentUser.getId().toString());

        try {
            List<SimplePerson> contacts = gPeopleService.getContacts(credential);

            return ResponseEntity.ok(contacts);
        } catch (IOException ie) {
            log.error("Problem retrieving contacts", ie);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ie.getMessage());
        } catch (GeneralSecurityException ge) {
            log.error("Security issue!", ge);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ge.getMessage());
        } catch (MessagingException me) {
            log.error("Messaging issue!", me);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(me.getMessage());
        }


    }

}
