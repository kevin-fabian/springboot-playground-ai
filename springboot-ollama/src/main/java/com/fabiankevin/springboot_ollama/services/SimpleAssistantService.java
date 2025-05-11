package com.fabiankevin.springboot_ollama.services;

import com.fabiankevin.springboot_ollama.clients.AiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SimpleAssistantService implements AssistantService {
    private final AiClient aiClient;

    @Override
    public String chat(String message) {
        return aiClient.chat(message);
    }

    @Override
    public Stream<String> chatStream(String message) {
        return Stream.empty();
    }
}
