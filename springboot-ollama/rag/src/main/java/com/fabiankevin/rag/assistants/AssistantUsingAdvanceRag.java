package com.fabiankevin.rag.assistants;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
@Primary
public class AssistantUsingAdvanceRag implements Assistant {
    private final ChatClient client;
    private final VectorStore vectorStore;
    private final ChatClient.Builder chatClientBuilder;

    @Override
    public Stream<String> ask(String userInput) {
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(RewriteQueryTransformer.builder()
                        .chatClientBuilder(chatClientBuilder.build().mutate())
                        .build())
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.5)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        return client.prompt()
                .system("""
                        Your response should be in UTF-8 format.
                        """)
                .advisors(retrievalAugmentationAdvisor)
                .user(userInput)
                .stream()
                .content()
                .toStream();
    }
}
