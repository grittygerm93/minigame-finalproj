package com.example.backend.service.oauth;

import com.example.backend.model.oauth.Email;
import com.example.backend.util.oauth.Constants;
import com.example.backend.util.oauth.EmailUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;

@Slf4j
@Service
public class GmailService {

    private Message createGmailMessageFromEmail(Email email) throws MessagingException, IOException {
        MimeMessage mimeMessage = EmailUtil.convertEmailToMimeMessage(email);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);

        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getEncoder().encodeToString(bytes);

        Message message = new Message();
        message.setRaw(encodedEmail);

        return message;
    }

    public void sendEmail(Email email, Credential credential) throws IOException, GeneralSecurityException, MessagingException {
        Gmail service = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), credential)
                .setApplicationName(Constants.APPLICATION_NAME)
                .build();

        Message message = createGmailMessageFromEmail(email);

        Message sentMessage = service
                .users()
                .messages()
                .send(Constants.CURRENT_USER, message)
                .execute();

//        Email sentEmail = getEmail(sentMessage, true);
//        return sentEmail;
    }

}
