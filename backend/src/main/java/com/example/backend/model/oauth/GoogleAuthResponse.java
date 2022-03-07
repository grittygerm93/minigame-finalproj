package com.example.backend.model.oauth;

import lombok.Data;

@Data
public class GoogleAuthResponse {
    private String code;
    private String redirectUrl;
}
