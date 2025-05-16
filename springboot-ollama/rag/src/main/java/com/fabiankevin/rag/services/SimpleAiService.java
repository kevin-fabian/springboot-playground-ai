package com.fabiankevin.rag.services;

import com.fabiankevin.rag.assistants.Assistant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class SimpleAiService implements AiService {
    private final Assistant assistant;

    @Override
    public Stream<String> ask(String userInput) {
        return assistant.ask(userInput);
    }
}
