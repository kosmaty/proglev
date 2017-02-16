package com.proglev.uploader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Uploader {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        String filePath = args[0];

        String refreshToken = System.getenv("REFRESH_TOKEN");
        String clientId = System.getenv("CLIENT_ID");
        String clientSecret = System.getenv("CLIENT_SECRET");
        String fileId = System.getenv("FILE_ID");

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Credential credentials = new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(httpTransport)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(refreshToken);

        Drive driveService = new Drive.Builder(
                httpTransport,
                JSON_FACTORY,
                credentials
        ).build();


        java.io.File fileToUpload = new java.io.File(filePath);
//        System.out.println(fileToUpload.getCanonicalPath());
        File fileMetadata = new File();
        fileMetadata.setName(fileToUpload.getName());
        fileMetadata.setMimeType("application/octet-stream");

        FileContent mediaContent = new FileContent("application/octet-stream", fileToUpload);
        driveService.files().update(fileId, fileMetadata, mediaContent).execute();
    }

}
