package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ServiceWithStreamingExample {

    interface Assistant {

        TokenStream chat(String message);
    }

    public static void main(String[] args) throws Exception {
        StreamingChatModel model  = OllamaStreamingChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(OllamaAIModel.GEMMA3_1B_IT_QAT)
                .temperature(0.7)
                .build();

        Assistant assistant = AiServices.create(Assistant.class, model);

        TokenStream tokenStream = assistant.chat("Tell me a joke");

        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        tokenStream.onPartialResponse(System.out::print)
                .onCompleteResponse(futureResponse::complete)
                .onError(futureResponse::completeExceptionally)
                .start();

        ChatResponse chatResponse = futureResponse.get(10, SECONDS);
        System.out.println("\n" + chatResponse);
    }
}