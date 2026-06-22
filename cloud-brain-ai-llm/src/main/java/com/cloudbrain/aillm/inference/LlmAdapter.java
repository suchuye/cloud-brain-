package com.cloudbrain.aillm.inference;

/**
 * Adapter interface for LLM provider implementations.
 * Part of the infrastructure layer — each implementation wraps a specific LLM provider
 * (OpenAI, local model, rule-based fallback, etc.) behind a uniform contract.
 */
public interface LlmAdapter {
    String chat(String systemPrompt, String userMessage);
    boolean isAvailable();
    String getName();
}
