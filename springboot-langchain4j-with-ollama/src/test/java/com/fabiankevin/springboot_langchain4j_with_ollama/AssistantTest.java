package com.fabiankevin.springboot_langchain4j_with_ollama;

import com.fabiankevin.springboot_langchain4j_with_ollama.assistants.Assistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssistantTest {

    @Autowired
    private Assistant assistant;

    @Test
    void simpleInteraction() {
        String answer = assistant.chat("Hello");
        System.out.println("Answer: "+answer);
    }
}
