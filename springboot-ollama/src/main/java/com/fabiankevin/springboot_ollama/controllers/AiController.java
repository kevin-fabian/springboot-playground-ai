package com.fabiankevin.springboot_ollama.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/v1/api/ai/generation")
@RequiredArgsConstructor
@Slf4j
public class AiController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @GetMapping
    public Map<String, String> generate(@RequestParam("message") String message) {
        log.info("Receive a request with a message = {}", message);

        return Map.of("completion", chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
//                        .advisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
//                        .advisors(new PromptChatMemoryAdvisor(new InMemoryChatMemory()))
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user(message)
                .call()
                .content());
    }

    @GetMapping("/streaming")
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        log.info("Receive a request with a message = {}", message);

        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor(), new QuestionAnswerAdvisor(vectorStore))
                .user(message)
                .stream().content();
    }
}
