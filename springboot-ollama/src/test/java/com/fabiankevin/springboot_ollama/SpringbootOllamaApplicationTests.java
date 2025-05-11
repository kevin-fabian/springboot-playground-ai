package com.fabiankevin.springboot_ollama;

import com.fabiankevin.springboot_ollama.models.ActorFilms;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@SpringBootTest
class SpringbootOllamaApplicationTests {

    @Autowired
    private ChatClient chatClient;

    @Test
    void returningEntity() {
        ActorFilms entity = chatClient.prompt()
                .user("Generate the filmography for Tom Cruise.")
                .call()
                .entity(ActorFilms.class);
        System.out.println(entity);
    }

    @Test
    void returningEntities() {
        List<ActorFilms> actorFilms = chatClient.prompt()
                .user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray.")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilms>>() {
                });
        System.out.println(actorFilms);
    }


    @Test
    void withPersona() {
        String response = chatClient.prompt()
                .user("Tell me a joke.")
                .system("You are a friendly chat bot that answers question in the voice of a Pirate")
                .call()
                .content();
        System.out.println(response);
    }





    @Test
    void pt_zero_shot() {
        enum Sentiment {
            POSITIVE, NEUTRAL, NEGATIVE
        }

        Sentiment reviewSentiment = chatClient.prompt("""
                        Classify movie reviews as POSITIVE, NEUTRAL or NEGATIVE.
                        Review: "Her" is a disturbing study revealing the direction
                        humanity is headed if AI is allowed to keep evolving,
                        unchecked. I wish there were more movies like this masterpiece.
                        Sentiment:
                        """)
                .options(ChatOptions.builder()
                        .temperature(0.1)
                        .model("gemma3:4b-it-qat")
                        .maxTokens(5)
                        .build())
                .call()
                .entity(Sentiment.class);

        System.out.println("Output: " + reviewSentiment);
    }

    @Value("classpath:fruits.jpg")
    private Resource imageResource;

    @Test
    void imageToText() {

        UserMessage userMessage = new UserMessage("Explain what do you see on this picture?",
                new Media(MimeTypeUtils.IMAGE_JPEG, this.imageResource));
        Prompt prompt = new Prompt(userMessage);

        String content = chatClient.prompt(prompt)
                .options(ChatOptions.builder()
                        .model("gemma3:4b-it-qat")
                        .build())
                .call()
                .content();
        System.out.println(content);
    }
}
