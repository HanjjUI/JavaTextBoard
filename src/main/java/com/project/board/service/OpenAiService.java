package com.project.board.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private static final String RESPONSES_URL = "https://api.openai.com/v1/responses";

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public OpenAiService(
            RestClient.Builder restClientBuilder,
            @Value("${app.openai.api-key:}") String apiKey,
            @Value("${app.openai.model:gpt-5-nano}") String model
    ) {
        this.restClient = restClientBuilder.build();
        this.apiKey = apiKey;
        this.model = model;
    }

    public String answer(String question) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is not configured");
        }

        String normalizedQuestion = question == null ? "" : question.trim();
        if (normalizedQuestion.isEmpty()) {
            throw new IllegalArgumentException("Question is required");
        }

        JsonNode response = restClient.post()
                .uri(RESPONSES_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createRequestBody(normalizedQuestion))
                .retrieve()
                .body(JsonNode.class);

        String answer = extractText(response);
        if (answer == null || answer.isBlank()) {
            throw new IllegalStateException("AI response was empty");
        }

        return answer.trim();
    }

    private Map<String, Object> createRequestBody(String question) {
        return Map.of(
                "model", model,
                "instructions", "Answer in Korean. Write a helpful board-post body for the user's title or question. Keep it practical, friendly, and ready to paste into a post.",
                "input", List.of(
                        Map.of(
                                "role", "user",
                                "content", List.of(
                                        Map.of(
                                                "type", "input_text",
                                                "text", question
                                        )
                                )
                        )
                ),
                "max_output_tokens", 700
        );
    }

    private String extractText(JsonNode response) {
        if (response == null) {
            return "";
        }

        JsonNode outputText = response.path("output_text");
        if (outputText.isTextual()) {
            return outputText.asText();
        }

        StringBuilder text = new StringBuilder();
        for (JsonNode output : response.path("output")) {
            for (JsonNode content : output.path("content")) {
                JsonNode textNode = content.path("text");
                if (textNode.isTextual()) {
                    text.append(textNode.asText());
                }
            }
        }

        return text.toString();
    }
}
