package com.example.backend.service.oauth;

import com.example.backend.model.oauth.SimplePerson;
import com.example.backend.util.oauth.Constants;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GPeopleService {

    public List<SimplePerson> getContacts(Credential credential) throws IOException, GeneralSecurityException, MessagingException {
        PeopleService service = new PeopleService.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), credential)
                .setApplicationName(Constants.APPLICATION_NAME)
                .build();

        ListConnectionsResponse response = service.people().connections().list("people/me")
                .setPersonFields("names,emailAddresses,coverPhotos")
                .setPageSize(10)
                .execute();
        List<Person> connections = response.getConnections();
        List<SimplePerson> simplePersonList = new ArrayList<>();

        if (connections != null && connections.size() > 0) {
            for (Person person : connections) {
                String name = "";
                String email = "";
                String coverPhotoUrl = "";
                if (person.getNames() != null)
                    name = person.getNames().get(0).getDisplayName();
                if (person.getEmailAddresses() != null)
                    email = person.getEmailAddresses().get(0).getValue();
                if (person.getCoverPhotos() != null)
                    coverPhotoUrl = person.getCoverPhotos().get(0).getUrl();
                simplePersonList.add(new SimplePerson(name, email, coverPhotoUrl));
            }
        }
        return simplePersonList;
    }


}
