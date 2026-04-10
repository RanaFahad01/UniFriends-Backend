package com.ranafahad.unifriends.onboarding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ProfanityCheckService {

    private final WebClient webClient;
    private final String apiKey;
    private static final Logger log = LoggerFactory.getLogger(ProfanityCheckService.class);

    public ProfanityCheckService(
            @Qualifier("profanityWebClient") WebClient webClient,
            @Value("${app.profanity.api-key}") String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

    @SuppressWarnings("unchecked")
    public boolean isFlagged(String text) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("text", text)
                            .build())
                    .header("x-api-key", apiKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && Boolean.TRUE.equals(response.get("has_profanity"))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            // Fail open — never let profanity API outage break onboarding
            log.warn("Profanity check failed, failing open: {}", e.getMessage());
            return false;
        }
    }
}
