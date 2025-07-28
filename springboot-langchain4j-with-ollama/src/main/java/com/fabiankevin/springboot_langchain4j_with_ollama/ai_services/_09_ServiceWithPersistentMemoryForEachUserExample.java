package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public class _09_ServiceWithPersistentMemoryForEachUserExample {
    private static final OllamaChatModel model = OllamaChatModel.builder()
            .baseUrl("http://localhost:11434")
            .modelName(OllamaAIModel.GEMMA3_1B_IT_QAT)
            .temperature(0.1)
            .build();

    interface Assistant {

        String chat(@MemoryId int memoryId, @UserMessage String userMessage);
    }

    public static void main(String[] args) {

//        PersistentChatMemoryStore store = new PersistentChatMemoryStore();

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
//                .chatMemoryStore(store)
                .build();


        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(chatMemoryProvider)
                .build();

        System.out.println(assistant.chat(1, "Hello, my name is Klaus"));
        System.out.println(assistant.chat(2, "Hi, my name is Francine"));

        // Now, comment out the two lines above, uncomment the two lines below, and run again.

        // System.out.println(assistant.chat(1, "What is my name?"));
        // System.out.println(assistant.chat(2, "What is my name?"));
    }

}