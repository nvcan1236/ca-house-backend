package com.nvc.user_service.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class FirebaseConfig {
    @Bean
    Firestore firestore() throws IOException {
        String path = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (path == null) {
            path = "user-service/toca-motel-firebase-adminsdk-h6b2n-73e6fc5f64.json"; // Chạy local
        }
        InputStream serviceAccount = new FileInputStream(path);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);
        return FirestoreClient.getFirestore();
    }
}
