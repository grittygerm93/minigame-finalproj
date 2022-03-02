package com.example.backend.repo;

import com.example.backend.entity.User;
import com.example.backend.entity.VerifyID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyIdRepo extends JpaRepository<VerifyID, Long> {
    VerifyID findByUser(User user);
}
