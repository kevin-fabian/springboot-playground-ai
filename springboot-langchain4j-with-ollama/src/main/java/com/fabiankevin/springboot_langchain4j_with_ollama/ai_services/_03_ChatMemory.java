package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;

import static com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel.GEMMA3_1B_IT_QAT;
import static dev.langchain4j.data.message.UserMessage.userMessage;

class _03_ChatMemory {

    public static void main(String args[]) {
        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(GEMMA3_1B_IT_QAT)
                .temperature(0.7)
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        SystemMessage systemMessage = SystemMessage.from(
                "You are a senior developer explaining to another senior developer, "
                        + "the project you are working on is an e-commerce platform with Java back-end, " +
                        "Oracle database, and Spring Data JPA");
        chatMemory.add(systemMessage);

        UserMessage userMessage1 = userMessage(
                "How do I optimize database queries for a large-scale e-commerce platform? "
                        + "Answer short in three to five lines maximum.");
        chatMemory.add(userMessage1);

        System.out.println("[User]: " + userMessage1.singleText());
        System.out.print("[LLM]: ");

        AiMessage aiMessage1 = model.chat(chatMemory.messages()).aiMessage();
        chatMemory.add(aiMessage1);
        System.out.println(aiMessage1.text());

        UserMessage userMessage2 = userMessage(
                "Give a concrete example implementation of the first point? " +
                        "Be short, 10 lines of code maximum.");
        chatMemory.add(userMessage2);

        System.out.println("\n\n[User]: " + userMessage2.singleText());
        System.out.print("[LLM]: ");

        AiMessage aiMessage2 = model.chat(chatMemory.messages()).aiMessage();
        chatMemory.add(aiMessage2);
        System.out.println(aiMessage2.text());

    }
}