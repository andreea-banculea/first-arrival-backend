package com.app.first_arrival.service;

import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class NotificationService {

    public void sendNotification(User user, Emergency emergency, Double distance) {
        try {
            String pushToken = user.getPushToken();
            if (pushToken == null) {
                throw new Exception("User does not have a push token");
            }
            PushData pushData = new PushData(emergency);

            NotificationPayload payload = new NotificationPayload(
                    22081,
                    "CskVox7GEbNREhcbeD6kmo",
                    "New Emergency Alert!",
                    String.format("An emergency has been reported %.2f km away. Please respond.", distance),
                    pushToken,
                    pushData
            );
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String requestBody = objectMapper.writeValueAsString(payload);

            String url = "https://app.nativenotify.com/api/notification";
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Notification sent successfully");
            } else {
                System.out.println("Failed to send notification");
            }
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    private static class NotificationPayload {
        public int appId;
        public String appToken;
        public String title;
        public String body;
        public String target;
        public PushData pushData;

        public NotificationPayload(int app_id, String appToken, String title, String body, String target, PushData pushData) {
            this.appId = app_id;
            this.appToken = appToken;
            this.title = title;
            this.body = body;
            this.target = target;
            this.pushData = pushData;
        }
    }

    private static class PushData {
        public Emergency emergencyId;

        public PushData(Emergency emergencyId) {
            this.emergencyId = emergencyId;
        }
    }
}
