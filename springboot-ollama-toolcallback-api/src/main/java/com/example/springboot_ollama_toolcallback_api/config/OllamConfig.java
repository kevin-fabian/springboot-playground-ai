package com.example.springboot_ollama_toolcallback_api.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamConfig {

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel){
        return ChatClient.create(ollamaChatModel);
    }
    @Bean
    public ChatMemory chatMemory(){
        return new InMemoryChatMemory();
    }
}
