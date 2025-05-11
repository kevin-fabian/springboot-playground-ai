package com.fabiankevin.springboot_ollama.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class OllamaAiClient implements AiClient {
    private final ChatClient client;

    @Override
    public Stream<String> chatStream(String message) {
        try {
            return client.prompt()
                    .user(message)
                    .stream()
                    .content()
                    .toStream();
        } catch (Exception e) {
            // Handle the exception as needed
            System.err.println("Error during chat: " + e.getMessage());
        }
        return Stream.empty();
    }

    @Override
    public String chat(String message) {
        try {
            return client.prompt()
                    .user(message)
                    .call()
                    .content();
        } catch (Exception e) {
            // Handle the exception as needed
            System.err.println("Error during chat: " + e.getMessage());
        }
        return "";
    }
}
