package com.fabiankevin.prompts;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.model.Content;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EvaluationTest {
    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Autowired
    private VectorStore vectorStore;


    @Test
    void testEvaluation() {
        String userText = "What is the purpose of Carina?";

        ChatResponse response = chatClient.prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .options(ChatOptions.builder()
                        .model("gemma3:4b-it-qat")
                        .build())
                .user(userText)
                .call()
                .chatResponse();
        String responseContent = response.getResult().getOutput().getContent();

        var relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);

        EvaluationRequest evaluationRequest = new EvaluationRequest(userText,
                response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS), responseContent);

        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertTrue(evaluationResponse.isPass(), "Response is not relevant to the question");
    }


    @Test
    void testFactChecking() {

        // Create the FactCheckingEvaluator
        var factCheckingEvaluator = new FactCheckingEvaluator(chatClientBuilder);

        // Example context and claim
        String context = "The Earth is the third planet from the Sun and the only astronomical object known to harbor life.";
        String claim = "The Earth is the fourth planet from the Sun.";

        // Create an EvaluationRequest
        EvaluationRequest evaluationRequest = new EvaluationRequest(context, Collections.emptyList(), claim);

        // Perform the evaluation
        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

        assertFalse(evaluationResponse.isPass(), "The claim should not be supported by the context");

    }
}
