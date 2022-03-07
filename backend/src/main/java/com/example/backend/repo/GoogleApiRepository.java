package com.example.backend.repo;

import com.example.backend.entity.GoogleApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleApiRepository extends JpaRepository<GoogleApi, Long> {

}