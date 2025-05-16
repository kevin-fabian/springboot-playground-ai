package com.fabiankevin.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class RagApplicationTests {

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Autowired
    private ChatClient chatClient;

    @Test
    void compressionQueryTransformer(){
        Query query = Query.builder()
                .text("And what is its second largest city?")
                .history(new UserMessage("What is the capital of the Philippines?"),
                        new AssistantMessage("Manila is the capital of the Philippines."))
                .build();

        QueryTransformer queryTransformer = CompressionQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder.defaultOptions(
                       ChatOptions.builder()
                               .model("gemma3:4b-it-qat")
                               .temperature(0.7)
                               .build()
                ))
                .build();

        Query transformedQuery = queryTransformer.transform(query);
        assertEquals("What is the second largest city in the Philippines?", transformedQuery.text().trim());
        System.out.println(transformedQuery.text());
    }

    @Test
    void rewriteQueryTransformer() {
        Query query = new Query("I'm running. What is the purpose of LLM?");

        QueryTransformer queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();

        Query transformedQuery = queryTransformer.transform(query);

        assertEquals("What is the purpose of Large Language Models (LLMs)?", transformedQuery.text().trim(), "The query should be rewritten to a more specific question.");
    }

    @Test
    void translationQueryTransformer(){
        Query query = new Query("Hvad er Danmarks hovedstad?");

        QueryTransformer queryTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .targetLanguage("english")
                .build();

        Query transformedQuery = queryTransformer.transform(query);
        assertEquals("What is the capital of Denmark?", transformedQuery.text().trim());
    }

    @Test
    void multiQueryExpander(){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = queryExpander.expand(new Query("How to run a Spring Boot app?"));
        assertEquals(4, queries.size(), "The number of expanded queries should be 4 including the original question.");
    }

    @Test
    void multiQueryExpanderIncludedOriginalFalse(){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .includeOriginal(false)
                .build();
        List<Query> queries = queryExpander.expand(new Query("How to run a Spring Boot app?"));
        assertEquals(3, queries.size(), "The number of expanded queries should be 3.");
    }


}
