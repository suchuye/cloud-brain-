package com.cloudbrain.aillm.inference.openai;

import com.cloudbrain.aillm.inference.LlmAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ai.llm.provider", havingValue = "openai", matchIfMissing = true)
public class OpenAiAdapter implements LlmAdapter {

    private static final Logger log = LoggerFactory.getLogger(OpenAiAdapter.class);

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String apiKey;
    private final String baseUrl;
    private final String model;

    public OpenAiAdapter(
            @Value("${ai.llm.openai.api-key}") String apiKey,
            @Value("${ai.llm.openai.base-url}") String baseUrl,
            @Value("${ai.llm.openai.model:gpt-4o-mini}") String model) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    @Override
    public String chat(String systemPrompt, String userMessage) {
        try {
            var body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userMessage)),
                    "temperature", 0.3,
                    "max_tokens", 800);

            var req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                log.error("OpenAI API error: {} {}", resp.statusCode(), resp.body());
                return null;
            }

            var tree = mapper.readTree(resp.body());
            return tree.at("/choices/0/message/content").asText();
        } catch (Exception e) {
            log.error("OpenAI call failed", e);
            return null;
        }
    }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public String getName() { return "OpenAI(" + model + ")"; }
}
