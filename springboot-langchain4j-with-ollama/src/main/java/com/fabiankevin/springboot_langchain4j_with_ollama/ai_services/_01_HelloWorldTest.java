package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

class _01_HelloWorldTest {

    public static void main(String[] args) {
        ChatModel chatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(OllamaAIModel.GEMMA3_1B_IT_QAT)
                .temperature(0.7)
                .build();

        String prompt = "Give me a Java code that prints 'Hello world' to the console.";

        String answer = chatModel.chat(prompt);
        System.out.println("Answer: "+answer);
    }

}
