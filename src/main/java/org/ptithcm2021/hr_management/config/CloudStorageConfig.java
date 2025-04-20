package org.ptithcm2021.hr_management.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Collections;

@Configuration
public class CloudStorageConfig {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name",cloudName,
                "api_key",apiKey,
                "api_secret",apiSecret
        ));
    }

    @Bean
    public Drive driveService() throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        InputStream in = new ClassPathResource("credentials.json").getInputStream();
        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Spring Boot Google Drive Upload")
                .build();
    }
}
