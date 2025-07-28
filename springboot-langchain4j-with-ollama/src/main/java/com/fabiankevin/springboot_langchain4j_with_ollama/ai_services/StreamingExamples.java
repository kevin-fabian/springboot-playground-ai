package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;
import static java.util.Arrays.asList;

public class StreamingExamples {

    static class StreamingChatModel_Example {

        public static void main(String[] args) {
            StreamingChatModel model  = OllamaStreamingChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(OllamaAIModel.GEMMA3_1B_IT_QAT)
                    .temperature(0.7)
                    .build();

            List<ChatMessage> messages = asList(
                    systemMessage("You are a very sarcastic assistant"),
                    userMessage("Tell me a joke")
            );

            CompletableFuture<ChatResponse> futureChatResponse = new CompletableFuture<>();

            model.chat(messages, new StreamingChatResponseHandler() {

                @Override
                public void onPartialResponse(String partialResponse) {
                    System.out.print(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    futureChatResponse.complete(completeResponse);
                }

                @Override
                public void onError(Throwable error) {
                    futureChatResponse.completeExceptionally(error);
                }
            });

            futureChatResponse.join();
        }
    }

    static class StreamingLanguageModel_Example {

        public static void main(String[] args) {

            StreamingChatModel model  = OllamaStreamingChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(OllamaAIModel.GEMMA3_1B_IT_QAT)
                    .temperature(0.7)
                    .build();

            CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

            model.chat("Tell me a joke", new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String s) {
                    System.out.println(s);
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    futureResponse.complete(chatResponse);
                }

                @Override
                public void onError(Throwable throwable) {
                    futureResponse.completeExceptionally(throwable);
                }
            });

            futureResponse.join();
        }
    }
}