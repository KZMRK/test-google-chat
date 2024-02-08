package com.kazmiruk.testgooglechat;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.chat.v1.HangoutsChat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;

@Configuration
public class AppConfig {

    private static final Collection<String> SCOPES =
            List.of(
                    "https://www.googleapis.com/auth/chat.spaces",
                    "https://www.googleapis.com/auth/chat.messages"
            );

    private static final java.io.File DATA_STORE_DIR =
            new java.io.File("store/auth-sample-app");

    @Bean
    public GsonFactory gsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    @Bean
    public GoogleClientSecrets googleClientSecrets() throws IOException {
         return GoogleClientSecrets.load(gsonFactory(),
                new InputStreamReader(TestGoogleChatApplication.class.getResourceAsStream("/client_secrets.json")));
    }

    @Bean
    public HttpTransport httpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public DataStoreFactory dataStoreFactory() throws IOException {
        return new FileDataStoreFactory(DATA_STORE_DIR);
    }

    @Bean
    public GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow() throws GeneralSecurityException, IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport(), gsonFactory(), googleClientSecrets(), SCOPES)
                .setDataStoreFactory(dataStoreFactory())
                .build();
    }

    @Bean
    public Credential userCredential() throws GeneralSecurityException, IOException {
        return new AuthorizationCodeInstalledApp(googleAuthorizationCodeFlow(), new LocalServerReceiver.Builder().setPort(9090).build()).authorize("user");
    }

    @Bean
    public HangoutsChat chatService() throws GeneralSecurityException, IOException {
        return new HangoutsChat.Builder(
                httpTransport(), gsonFactory(), userCredential())
                .setApplicationName("MessageExtractor")
                .build();
    }
}
