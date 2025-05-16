package com.fabiankevin.rag.assistants;

import java.util.stream.Stream;

public interface Assistant {
    Stream<String> ask(String userInput);
}
