package com.example.backend.service.oauth;

import com.example.backend.entity.GoogleApi;
import com.example.backend.entity.User;
import com.example.backend.repo.UserRepo;
import com.example.backend.util.oauth.AuthorizationUtil;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class GoogleApiDataStore<V extends Serializable> extends AbstractDataStore<V> {
    private UserRepo userRepository;

    private AuthorizationUtil authorizationUtil;

    private final Lock lock = new ReentrantLock();


    protected GoogleApiDataStore(DataStoreFactory dataStoreFactory, String id, UserRepo userRepository
            , AuthorizationUtil authorizationUtil) {
        super(dataStoreFactory, id);
        this.userRepository = userRepository;
        this.authorizationUtil = authorizationUtil;
    }


    @Override
    public Set<String> keySet() throws IOException {
        return null;
    }


    @Override
    public Collection<V> values() throws IOException {
        return null;
    }


    @Override
    public V get(String key) throws IOException {
        if (key != null) {
            lock.lock();

            try {
                User user = authorizationUtil.getCurrentUser();

                if (user.getId().toString().equals(key)) {
                    GoogleApi googleApi = user.getGoogleApi();

                    if (googleApi != null && googleApi.getStoredCredential() != null) {
                        return IOUtils.deserialize(googleApi.getStoredCredential());
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        return null;
    }


    @Override
    public DataStore<V> set(String key, V value) throws IOException {
        if (key != null) {
            lock.lock();

            try {
                User user = authorizationUtil.getCurrentUser();

                if (user.getId().toString().equals(key)) {
                    GoogleApi googleApi = user.getGoogleApi();
                    if (googleApi == null ) googleApi = new GoogleApi();

                    googleApi.setStoredCredential(IOUtils.serialize(value));
                    log.info("googleapi credentials {}", googleApi.getStoredCredential());

                    user.setGoogleApi(googleApi);

                    log.info("googleapi credentials in user {}", user.getGoogleApi().getStoredCredential());

                    userRepository.save(user);
                }
            } finally {
                lock.unlock();
            }
        }

        return this;
    }


    @Override
    public DataStore<V> clear() throws IOException {
        return null;
    }


    @Override
    public DataStore<V> delete(String key) throws IOException {
        return null;
    }
}