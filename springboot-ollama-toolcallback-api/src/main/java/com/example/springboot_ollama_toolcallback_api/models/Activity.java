package com.example.springboot_ollama_toolcallback_api.models;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Activity(UUID id,
                       String name,
                       String description,
                       Instant createdAt) {
}
