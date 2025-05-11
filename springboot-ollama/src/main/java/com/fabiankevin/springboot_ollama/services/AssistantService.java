package com.fabiankevin.springboot_ollama.services;

import java.util.stream.Stream;

public interface AssistantService {
    String chat(String message);
    Stream<String> chatStream(String message);
}
