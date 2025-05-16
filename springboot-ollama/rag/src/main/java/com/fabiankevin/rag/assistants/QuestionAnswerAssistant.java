package com.fabiankevin.rag.assistants;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class QuestionAnswerAssistant implements Assistant {
    private final ChatClient client;
    private final VectorStore vectorStore;

    @Override
    public Stream<String> ask(String userInput) {
        var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.8d)
                        .topK(3).build())
                .build();

        return client.prompt()
                .system("""
                        Your response should be easy to understand that even grade school students can understand.
                        Summerize your response in 3 sentences or less.
                        """)
                .advisors(qaAdvisor)
                .user(userInput)
                .options(ChatOptions.builder()
                        .temperature(0.5)
                        .build())
                .stream()
                .content()
                .toStream();
    }



}
