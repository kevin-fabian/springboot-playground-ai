package com.fabiankevin.springboot_ollama.controllers;

import com.fabiankevin.springboot_ollama.services.AssistantService;
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
@RequestMapping("/v1/api/ai/assistant")
@RequiredArgsConstructor
@Slf4j
public class AiController {
    private final AssistantService assistantService;

//    @GetMapping("/streaming")
//    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        log.info("Receive a request with a message = {}", message);
//
//        return chatClient.prompt()
//                .advisors(new SimpleLoggerAdvisor(), new QuestionAnswerAdvisor(vectorStore))
//                .user(message)
//                .stream().content();
//    }
}
