package com.fabiankevin.springboot_ollama.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SimpleAssistantServiceTest {

    @Autowired
    private AssistantService assistantService;

    @Test
    void chat_thenShouldSucceed() {
        String userMessage = "Hello, how are you?";
        String response = assistantService.chat(userMessage);

        assertNotNull(response);
    }
}