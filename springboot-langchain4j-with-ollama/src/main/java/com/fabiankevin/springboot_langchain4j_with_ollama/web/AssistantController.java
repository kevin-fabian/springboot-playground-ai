package com.fabiankevin.springboot_langchain4j_with_ollama.web;

import com.fabiankevin.springboot_langchain4j_with_ollama.assistants.Assistant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ai/assistant")
@RequiredArgsConstructor
@Slf4j
public class AssistantController {
    private final Assistant assistant;

//    @GetMapping
//    public String hello(){
//        return "Hello";
//    }

    @GetMapping
    public Flux<String> askAi(@RequestParam(defaultValue = "Greetings") String prompt){
        log.info("Request received: {}", prompt);
        return assistant.chat( prompt);
    }
}
