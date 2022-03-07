package com.example.backend.service.oauth;

import com.example.backend.repo.UserRepo;
import com.example.backend.util.oauth.AuthorizationUtil;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class GoogleApiDataStoreFactory extends AbstractDataStoreFactory {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private AuthorizationUtil authorizationUtil;


    @Override
    protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
        return new GoogleApiDataStore<V>(this, id, userRepository, authorizationUtil);
    }
}
