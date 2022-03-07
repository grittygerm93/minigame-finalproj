package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleApi {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "BLOB")
    private byte[] storedCredential;

//    @OneToOne(mappedBy = "googleApi", cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn
//    private User user;

}
