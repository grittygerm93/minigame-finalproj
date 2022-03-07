package com.example.backend.model.oauth;

import lombok.Data;

@Data
public class Email {
    private String html;
    private String plainText;
    private String subject;
    private Long date;
    private String from;
    private String to;
    private String id;
    private String snippet;
}

