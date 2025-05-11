package com.fabiankevin.springboot_ollama.clients;

import java.util.stream.Stream;

public interface AiClient {
    Stream<String> chatStream(String message);
    String chat(String message);
}
