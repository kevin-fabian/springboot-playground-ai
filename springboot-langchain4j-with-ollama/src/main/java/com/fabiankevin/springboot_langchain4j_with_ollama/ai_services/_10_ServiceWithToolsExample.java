package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;

public class _10_ServiceWithToolsExample {

    // Please also check CustomerSupportApplication and CustomerSupportApplicationTest
    // from spring-boot-example module

    static class Calculator {

        @Tool("Calculates the length of a string")
        int stringLength(String s) {
            System.out.println("Called stringLength() with s='" + s + "'");
            return s.length();
        }

        @Tool("Calculates the sum of two numbers")
        int add(int a, int b) {
            System.out.println("Called add() with a=" + a + ", b=" + b);
            return a + b;
        }

        @Tool("Calculates the square root of a number")
        double sqrt(int x) {
            System.out.println("Called sqrt() with x=" + x);
            return Math.sqrt(x);
        }
    }

    static class BasicCalculatorTool {

        @Tool("Calculates the sum of two numbers")
        int add(int a, int b) {
            System.out.println("Called add() with a=" + a + ", b=" + b);
            return a + b;
        }

        @Tool("Calculates the square root of a number")
        double sqrt(@P("The input to compute the square root") int x) {
            System.out.println("Called sqrt() with x=" + x);
            return Math.sqrt(x);
        }
    }

    interface Assistant {

        String chat(String userMessage);

        Result<String> chatWithTools(String userMessage);
    }

    public static void main(String[] args) {

        ChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("mistral:7b")
                .temperature(0.0)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .tools(new BasicCalculatorTool())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String question1 = "What is 5 + 100?";
        String answer1 = assistant.chat(question1);
        System.out.println(answer1);

        String question2 = "What is square root of 12345678?";
        Result<String> stringResult = assistant.chatWithTools(question2);
        System.out.println(stringResult.content());
        System.out.println(stringResult.toolExecutions());

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(UserMessage.userMessage(question2))
                .toolSpecifications(ToolSpecifications.toolSpecificationsFrom(BasicCalculatorTool.class))
                .build();
        ChatResponse chat = model.chat(chatRequest);
        System.out.println(chat.aiMessage().text());
    }
}