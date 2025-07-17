package com.example.back_law_office.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.io.IOException;
import java.util.Collections;

@Component
public class GoogleTokenVerifier {
    private static final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    @Value("${google.clientId:}")
    private String googleClientId;

    public GoogleIdToken.Payload verify(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            return null;
        }
    }
} 