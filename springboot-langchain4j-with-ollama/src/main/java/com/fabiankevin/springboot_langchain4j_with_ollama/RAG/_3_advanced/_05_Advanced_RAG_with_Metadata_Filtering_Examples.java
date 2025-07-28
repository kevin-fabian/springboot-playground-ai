package com.fabiankevin.springboot_langchain4j_with_ollama.RAG._3_advanced;

import com.fabiankevin.springboot_langchain4j_with_ollama.RAG.Assistant;
import com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.function.Function;

import static dev.langchain4j.data.document.Metadata.metadata;
import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

class _05_Advanced_RAG_with_Metadata_Filtering_Examples {
    private static final ChatModel model = OllamaChatModel.builder()
            .baseUrl("http://localhost:11434")
            .modelName(OllamaAIModel.GEMMA3_4B_IT_QAT)
            .temperature(0.0)
            .logRequests(true)
            .build();
    private static final EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();


    public static void main(String[] args) {
        // given
        TextSegment dogsSegment = TextSegment.from("Article about dogs ...", metadata("animal", "dog"));
        TextSegment birdsSegment = TextSegment.from("Article about birds ...", metadata("animal", "bird"));

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.add(embeddingModel.embed(dogsSegment).content(), dogsSegment);
        embeddingStore.add(embeddingModel.embed(birdsSegment).content(), birdsSegment);
        // embeddingStore contains segments about both dogs and birds

        Filter onlyDogs = metadataKey("animal").isEqualTo("dog");

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .filter(onlyDogs) // by specifying the static filter, we limit the search to segments only about dogs
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .contentRetriever(contentRetriever)
                .build();

        // when
        String answer = assistant.answer("Which animal?");
        System.out.println("Answer: "+answer);
        System.out.println(answer.contains("dog"));
        System.out.println(answer.contains("bird"));

    }


    interface PersonalizedAssistant {

        String chat(@MemoryId String userId, @UserMessage String userMessage);
    }

    static class Dynamic_Metadata_Filter_Example {
        public static void main(String[] args) {
            // given
            TextSegment user1Info = TextSegment.from("My favorite color is green", metadata("userId", "1"));
            TextSegment user2Info = TextSegment.from("My favorite color is red", metadata("userId", "2"));

            EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
            embeddingStore.add(embeddingModel.embed(user1Info).content(), user1Info);
            embeddingStore.add(embeddingModel.embed(user2Info).content(), user2Info);
            // embeddingStore contains information about both first and second user

            Function<Query, Filter> filterByUserId =
                    (query) -> metadataKey("userId").isEqualTo(query.metadata().chatMemoryId().toString());

            ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(embeddingStore)
                    .embeddingModel(embeddingModel)
                    // by specifying the dynamic filter, we limit the search to segments that belong only to the current user
                    .dynamicFilter(filterByUserId)
                    .build();

            PersonalizedAssistant personalizedAssistant = AiServices.builder(PersonalizedAssistant.class)
                    .chatModel(model)
                    .contentRetriever(contentRetriever)
//                    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                    .build();

            // when
            String answer1 = personalizedAssistant.chat("1", "Which color would be best for a dress?");


            System.out.println("Answer 1" + answer1.contains("green"));
            System.out.println("Answer 1" + answer1.contains("red"));

            // when
            String answer2 = personalizedAssistant.chat("2", "Which color would be best for a dress?");

            System.out.println("Answer 2" + answer2.contains("red"));
            System.out.println("Answer 2" + answer2.contains("green"));
        }
    }

    static class LLM_generated_Metadata_Filter_Example {

    }


//
//    @Test
//    void LLM_generated_Metadata_Filter_Example() {
//
//        // given
//        TextSegment forrestGump = TextSegment.from("Forrest Gump", metadata("genre", "drama").put("year", 1994));
//        TextSegment groundhogDay = TextSegment.from("Groundhog Day", metadata("genre", "comedy").put("year", 1993));
//        TextSegment dieHard = TextSegment.from("Die Hard", metadata("genre", "action").put("year", 1998));
//
//        // describe metadata keys as if they were columns in the SQL table
//        TableDefinition tableDefinition = TableDefinition.builder()
//                .name("movies")
//                .addColumn("genre", "VARCHAR", "one of: [comedy, drama, action]")
//                .addColumn("year", "INT")
//                .build();
//
//        LanguageModelSqlFilterBuilder sqlFilterBuilder = new LanguageModelSqlFilterBuilder(chatModel, tableDefinition);
//
//        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//        embeddingStore.add(embeddingModel.embed(forrestGump).content(), forrestGump);
//        embeddingStore.add(embeddingModel.embed(groundhogDay).content(), groundhogDay);
//        embeddingStore.add(embeddingModel.embed(dieHard).content(), dieHard);
//
//        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
//                .embeddingStore(embeddingStore)
//                .embeddingModel(embeddingModel)
//                .dynamicFilter(query -> sqlFilterBuilder.build(query)) // LLM will generate the filter dynamically
//                .build();
//
//        Assistant assistant = AiServices.builder(Assistant.class)
//                .chatModel(chatModel)
//                .contentRetriever(contentRetriever)
//                .build();
//
//        // when
//        String answer = assistant.answer("Recommend me a good drama from 90s");
//
//        // then
//        assertThat(answer)
//                .containsIgnoringCase("Forrest Gump")
//                .doesNotContainIgnoringCase("Groundhog Day")
//                .doesNotContainIgnoringCase("Die Hard");
//    }
}