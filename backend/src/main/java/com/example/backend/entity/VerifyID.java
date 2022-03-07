package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class VerifyID {

    //    expires in 5min
//    private final int EXPIRATION_TIME = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String verifyId;
    private LocalDateTime expirationTime;

    @OneToOne()
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerifyID(String verifyId, User user) {
        this.user = user;
        this.verifyId = verifyId;
        this.expirationTime = calculateExpirationTime(5);
    }

    private LocalDateTime calculateExpirationTime(int expiration_time) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusMinutes(expiration_time);
    }

}
