package com.fabiankevin.springboot_langchain4j_with_ollama.assistants;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService(chatModel = "ollamaChatModel")
public interface Assistant {

//    @SystemMessage("You are a polite assistant")
//    Flux<String> chat(@MemoryId int memoryId, @UserMessage String message);

    @SystemMessage("You are a polite assistant")
    Flux<String> chat( String message);
}