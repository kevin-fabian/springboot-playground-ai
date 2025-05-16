package com.fabiankevin.rag.services;

import java.util.stream.Stream;

public interface AiService {
    Stream<String> ask(String userInput);
}
