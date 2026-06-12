package com.cloudbrain.aillm.inference;

public interface LlmAdapter {
    String chat(String systemPrompt, String userMessage);
    boolean isAvailable();
    String getName();
}
