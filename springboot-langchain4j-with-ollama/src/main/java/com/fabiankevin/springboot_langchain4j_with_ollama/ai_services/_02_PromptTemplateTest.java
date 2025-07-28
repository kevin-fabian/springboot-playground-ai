package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel.GEMMA3_1B_IT_QAT;
import static java.util.Arrays.asList;

class _02_PromptTemplateTest {
    @StructuredPrompt({
            "Create a recipe of a {{dish}} that can be prepared using only {{ingredients}}.",
            "Structure your answer in the following way:",

            "Recipe name: ...",
            "Description: ...",
            "Preparation time: ...",

            "Required ingredients:",
            "- ...",
            "- ...",

            "Instructions:",
            "- ...",
            "- ..."
    })
    static class CreateRecipePrompt {

        String dish;
        List<String> ingredients;

        CreateRecipePrompt(String dish, List<String> ingredients) {
            this.dish = dish;
            this.ingredients = ingredients;
        }
    }


    public static void main(String[] args) {
        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(GEMMA3_1B_IT_QAT)
                .temperature(0.7)
                .build();

        CreateRecipePrompt createRecipePrompt = new CreateRecipePrompt(
                "salad",
                asList("cucumber", "tomato", "feta", "onion", "olives")
        );

        Prompt prompt = StructuredPromptProcessor.toPrompt(createRecipePrompt);

        String recipe = model.chat(prompt.text());

        System.out.println(recipe);
    }

    static class PromptTemplate_with_One_Variable_Example {

        public static void main(String[] args) {

            PromptTemplate promptTemplate = PromptTemplate.from("Say 'hi' in {{it}}.");

            Prompt prompt = promptTemplate.apply("German");

            System.out.println(prompt.text()); // Say 'hi' in German.
        }
    }

    static class PromptTemplate_With_Multiple_Variables_Example {

        public static void main(String[] args) {

            PromptTemplate promptTemplate = PromptTemplate.from("Say '{{text}}' in {{language}}.");

            Map<String, Object> variables = new HashMap<>();
            variables.put("text", "hi");
            variables.put("language", "German");

            Prompt prompt = promptTemplate.apply(variables);

            System.out.println(prompt.text()); // Say 'hi' in German.
        }
    }

}

