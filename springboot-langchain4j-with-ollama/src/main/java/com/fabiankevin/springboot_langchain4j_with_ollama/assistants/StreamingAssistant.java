package com.fabiankevin.springboot_langchain4j_with_ollama.assistants;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService
public interface StreamingAssistant {

    @SystemMessage("You are a polite assistant")
    Flux<String> chat(String userMessage);
}