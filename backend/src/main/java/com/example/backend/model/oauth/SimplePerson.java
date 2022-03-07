package com.example.backend.model.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimplePerson {
    private String name;
    private String email;
    private String photoURL;
}
