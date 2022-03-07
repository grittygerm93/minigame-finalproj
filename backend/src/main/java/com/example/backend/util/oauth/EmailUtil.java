package com.example.backend.util.oauth;

import com.example.backend.model.oauth.Email;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    public static MimeMessage convertEmailToMimeMessage(Email email) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage mimeMessage = new MimeMessage(session);
//      with no from, defaults to the signed in user
//        mimeMessage.setFrom(new InternetAddress(email.getFrom()));
        mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(email.getTo()));
        mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getPlainText());

        return mimeMessage;
    }
}
