package com.fabiankevin.prompts;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@SpringBootTest
public class ImageToTextTest {

    @Autowired
    private ChatClient chatClient;

    @Value("classpath:images/fruits.jpg")
    private Resource imageResource;

    @Test
    void givenFruitImage_thenShouldBeMappedToFruitsEntity() {

        record Fruits(String name, String color, String shape) {
        }

        UserMessage userMessage = new UserMessage("Explain what do you see on this picture?",
                new Media(MimeTypeUtils.IMAGE_JPEG, this.imageResource));
        Prompt prompt = new Prompt(userMessage);

        List<Fruits> fruits = chatClient.prompt(prompt)
                .options(ChatOptions.builder()
                        .model("gemma3:4b-it-qat")
                        .build())
                .call()
                .entity(new ParameterizedTypeReference<>() {});

        System.out.println(fruits);
    }
}
