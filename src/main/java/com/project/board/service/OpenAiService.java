package com.project.board.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

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
        this.model = normalizeModel(model);
    }

    public String answer(String question, String language) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY is not configured");
        }

        String normalizedQuestion = question == null ? "" : question.trim();
        if (normalizedQuestion.isEmpty()) {
            throw new IllegalArgumentException("Question is required");
        }

        JsonNode response;
        try {
            response = restClient.post()
                    .uri(RESPONSES_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey.trim())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createRequestBody(normalizedQuestion, resolveLanguageInstruction(normalizedQuestion, language)))
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientResponseException e) {
            throw new IllegalStateException("OpenAI API error: " + extractErrorMessage(e));
        }

        String answer = extractText(response);
        if (answer == null || answer.isBlank()) {
            throw new IllegalStateException("AI response was empty: " + summarizeResponse(response));
        }

        return answer.trim();
    }

    private Map<String, Object> createRequestBody(String question, String languageInstruction) {
        return Map.of(
                "model", model,
                "instructions", languageInstruction + " Write a helpful board-post body for the user's title or question. Keep it practical, friendly, and ready to paste into a post.",
                "reasoning", Map.of(
                        "effort", "minimal"
                ),
                "text", Map.of(
                        "verbosity", "low"
                ),
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
                "max_output_tokens", 2000
        );
    }

    private String detectLanguageInstruction(String question) {
        if (question.matches(".*[\\u3040-\\u30ff].*")) {
            return "Answer only in Japanese.";
        }

        if (question.matches(".*[\\uac00-\\ud7af].*")) {
            return "Answer only in Korean.";
        }

        return "Detect the user's language and answer in the same language.";
    }

    private String resolveLanguageInstruction(String question, String language) {
        String normalizedLanguage = language == null ? "ja" : language.trim().toLowerCase();

        return switch (normalizedLanguage) {
            case "auto" -> detectLanguageInstruction(question);
            case "ko" -> "Answer only in Korean.";
            case "en" -> "Answer only in English.";
            case "ja" -> "Answer only in Japanese.";
            default -> "Answer only in Japanese.";
        };
    }

    private String normalizeModel(String value) {
        String normalizedModel = value == null ? "" : value.replaceAll("\\s+", "");
        if (normalizedModel.isBlank()) {
            return "gpt-5-nano";
        }
        return normalizedModel;
    }

    private String extractErrorMessage(RestClientResponseException e) {
        try {
            JsonNode error = e.getResponseBodyAs(JsonNode.class).path("error").path("message");
            if (error.isTextual() && !error.asText().isBlank()) {
                return error.asText();
            }
        } catch (RuntimeException ignored) {
            // Fall back to the HTTP status when the response body is not JSON.
        }

        return e.getStatusCode() + " " + e.getStatusText();
    }

    private String summarizeResponse(JsonNode response) {
        if (response == null) {
            return "no response body";
        }

        String status = response.path("status").asText("unknown status");
        String incompleteReason = response.path("incomplete_details").path("reason").asText("");

        if (!incompleteReason.isBlank()) {
            return status + " (" + incompleteReason + ")";
        }

        return status;
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
