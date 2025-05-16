package com.fabiankevin.rag.web;

import com.fabiankevin.rag.services.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequestMapping("/assistant")
@RestController
@RequiredArgsConstructor
public class SpringAssistantController {
    private final AiService aiService;

    @GetMapping(value = "/queries/{query}")
    public Flux<String> ask(@PathVariable String query) {
        return Flux.fromStream(aiService.ask(query));
    }
}
