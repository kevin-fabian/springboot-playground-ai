package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Moderate;
import dev.langchain4j.service.ModerationException;

import java.util.List;

// This doesn't work. I don't know which is the ModerateLevel implementation for Ollama
public class ServiceWithAutoModerationExample {

    interface Chat {

        @Moderate
        String chat(String text);
    }

    public static void main(String[] args) {

        ChatModel chatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(OllamaAIModel.GEMMA3_1B_IT_QAT)
                .temperature(0.7)
                .build();

        Chat chat = AiServices.builder(Chat.class)
                .chatModel(chatModel)
                .moderationModel(new ModerationModel() {
                    @Override
                    public Response<Moderation> moderate(String s) {
                        return null;
                    }

                    @Override
                    public Response<Moderation> moderate(List<ChatMessage> list) {
                        return null;
                    }
                })
//                .moderationModel(moderationModel)
                .build();

        try {
            chat.chat("I WILL KILL YOU!!!");
        } catch (ModerationException e) {
            System.out.println(e.getMessage());
            // Text "I WILL KILL YOU!!!" violates content policy
        }
    }
}