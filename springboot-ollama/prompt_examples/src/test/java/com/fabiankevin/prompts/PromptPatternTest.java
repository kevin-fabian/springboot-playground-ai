package com.fabiankevin.prompts;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class PromptPatternTest {

    @Autowired
    private ChatClient chatClient;

    public record ActorFilms(String actor, List<String> movies) {}


    @Test
    void returningEntity() {
        ActorFilms entity = chatClient.prompt()
                .user("Generate the filmography for Tom Cruise.")
                .call()
                .entity(ActorFilms.class);
        
        assertEquals("Tom Cruise", entity.actor());
        assertFalse(entity.movies().isEmpty(), "should not be empty");
    }

    @Test
    void returningEntities() {
        List<ActorFilms> actorFilms = chatClient.prompt()
                .user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray.")
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });

        assertFalse(actorFilms.isEmpty(), "should not be empty");
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
    void zero_shot() {
        record Sentiment(String sentiment) {
        }

        Sentiment reviewSentiment = chatClient.prompt("""
                        Classify movie reviews as POSITIVE, NEUTRAL or NEGATIVE.
                        Review: "Her" is a disturbing study revealing the direction
                        humanity is headed if AI is allowed to keep evolving,
                        unchecked. I wish there were more movies like this masterpiece.
                        Sentiment:
                        """)
                .options(ChatOptions.builder()
                        .model("gemma3:4b-it-qat")
                        .temperature(0.1)
                        .maxTokens(50)
                        .build())
                .call()
                .entity(Sentiment.class);

        assertEquals("POSITIVE", reviewSentiment.sentiment());
    }

    @Test
    public void one_shot_few_shots() {
        String pizzaOrder = chatClient.prompt("""
                        Parse a customer's pizza order into valid JSON

                        EXAMPLE 1:
                        I want a small pizza with cheese, tomato sauce, and pepperoni.
                        JSON Response:
                        ```
                        {
                            "size": "small",
                            "type": "normal",
                            "ingredients": ["cheese", "tomato sauce", "pepperoni"]
                        }
                        ```

                        EXAMPLE 2:
                        Can I get a large pizza with tomato sauce, basil and mozzarella.
                        JSON Response:
                        ```
                        {
                            "size": "large",
                            "type": "normal",
                            "ingredients": ["tomato sauce", "basil", "mozzarella"]
                        }
                        ```

                        Now, I would like a large pizza, with the first half cheese and mozzarella.
                        And the other tomato sauce, ham and pineapple.
                        """)
                .options(ChatOptions.builder()
                        .model("gemma3:4b-it-qat")
                        .temperature(0.1)
                        .maxTokens(250)
                        .build())
                .call()
                .content();
        System.out.println("Output: " + pizzaOrder);
    }


    @Test
    public void system_prompting_1() {
        String movieReview = chatClient
                .prompt()
                .system("Classify movie reviews as positive, neutral or negative. Only return the label in uppercase.")
                .user("""
                        Review: "Her" is a disturbing study revealing the direction
                        humanity is headed if AI is allowed to keep evolving,
                        unchecked. It's so disturbing I couldn't watch it.

                        Sentiment:
                        """)
                .options(ChatOptions.builder()
                        .model("gemma3:4b-it-qat")
                        .temperature(1.0)
                        .topK(40)
                        .topP(0.8)
                        .maxTokens(5)
                        .build())
                .call()
                .content();
        System.out.println("Output: " + movieReview);
    }


    @Test
    void system_prompting_with_spring_ai_entity_mapping() {
        record MovieReviews(Movie[] movieReviews) {
            enum Sentiment {
                POSITIVE, NEUTRAL, NEGATIVE
            }

            record Movie(Sentiment sentiment, String name) {
            }
        }

        MovieReviews movieReviews = chatClient
                .prompt()
                .system("""
                        Classify movie reviews as positive, neutral or negative. Return
                        valid JSON.
                        """)
                .user("""
                        Review: "Her" is a disturbing study revealing the direction
                        humanity is headed if AI is allowed to keep evolving,
                        unchecked. It's so disturbing I couldn't watch it.

                        JSON Response:
                        """)
                .call()
                .entity(MovieReviews.class);

        System.out.println("Output: "+ Arrays.toString(movieReviews.movieReviews()) );
    }

    @Test
    public void role_prompting_1() {
        String travelSuggestions = chatClient
                .prompt()
                .system("""
                    I want you to act as a travel guide. I will write to you
                    about my location and you will suggest 3 places to visit near
                    me. In some cases, I will also give you the type of places I
                    will visit.
                    """)
                .user("""
                    My suggestion: "I am in Amsterdam and I want to visit only museums."
                    Travel Suggestions:
                    """)
                .call()
                .content();
        System.out.println(travelSuggestions);
    }

    @Test
     void role_prompting_2() {
        String humorousTravelSuggestions = chatClient
                .prompt()
                .system("""
                    I want you to act as a travel guide. I will write to you about
                    my location and you will suggest 3 places to visit near me in
                    a humorous style.
                    """)
                .user("""
                    My suggestion: "I am in Amsterdam and I want to visit only museums."
                    Travel Suggestions:
                    """)
                .call()
                .content();
        System.out.println("Output: "+humorousTravelSuggestions );
    }

    @Test
    void pt_contextual_prompting() {
        String articleSuggestions = chatClient
                .prompt()
                .user(u -> u.text("""
                    Suggest 3 topics to write an article about with a few lines of
                    description of what this article should contain.

                    Context: {context}
                    """)
                        .param("context", "You are writing for a blog about retro 80's arcade video games."))
                .call()
                .content();
        System.out.println("Output: "+articleSuggestions );

    }

    @Test
    public void step_back_prompting() {
        ChatOptions chatOptions = ChatOptions.builder()
                .model("gemma3:4b-it-qat")
                .temperature(1.0)
                .topK(40)
                .topP(0.8)
                .maxTokens(1024)
                .build();


        // First get high-level concepts
        String stepBack = chatClient
                .prompt("""
                    Based on popular first-person shooter action games, what are
                    5 fictional key settings that contribute to a challenging and
                    engaging level storyline in a first-person shooter video game?
                    """)
                .options(chatOptions)
                .call()
                .content();

        // Then use those concepts in the main task
        String story = chatClient
                .prompt()
                .user(u -> u.text("""
                    Write a one paragraph storyline for a new level of a first-
                    person shooter video game that is challenging and engaging.

                    Context: {step-back}
                    """)
                        .param("step-back", stepBack))
                .options(chatOptions)
                .call()
                .content();

        System.out.println("Story: "+story);
    }

    @Test
    public void chain_of_thought_zero_shot() {
        String output = chatClient
                .prompt("""
                    When I was 3 years old, my partner was 3 times my age. Now,
                    I am 20 years old. How old is my partner?

                    Let's think step by step.
                    """)
                .options(ChatOptions.builder()
                        .model("gemma3:4b-it-qat")
                        .build())
                .call()
                .content();

        System.out.println("Output: "+output);

    }

    @Test
    public void pt_chain_of_thought_singleshot_fewshots() {
        String output = chatClient
                .prompt("""
                    Q: When my brother was 2 years old, I was double his age. Now
                    I am 40 years old. How old is my brother? Let's think step
                    by step.
                    A: When my brother was 2 years, I was 2 * 2 = 4 years old.
                    That's an age difference of 2 years and I am older. Now I am 40
                    years old, so my brother is 40 - 2 = 38 years old. The answer
                    is 38.
                    Q: When I was 3 years old, my partner was 3 times my age. Now,
                    I am 20 years old. How old is my partner? Let's think step
                    by step.
                    A:
                    """)
                .call()
                .content();
        System.out.println("output: "+output);
    }

    @Test
    public void pt_self_consistency() {
        String email = """
            Hi,
            I have seen you use Wordpress for your website. A great open
            source content management system. I have used it in the past
            too. It comes with lots of great user plugins. And it's pretty
            easy to set up.
            I did notice a bug in the contact form, which happens when
            you select the name field. See the attached screenshot of me
            entering text in the name field. Notice the JavaScript alert
            box that I inv0k3d.
            But for the rest it's a great website. I enjoy reading it. Feel
            free to leave the bug in the website, because it gives me more
            interesting things to read.
            Cheers,
            Harry the Hacker.
            """;

        record EmailClassification(Classification classification, String reasoning) {
            enum Classification {
                IMPORTANT, NOT_IMPORTANT
            }
        }

        int importantCount = 0;
        int notImportantCount = 0;

        // Run the model 5 times with the same input
        for (int i = 0; i < 5; i++) {
            EmailClassification output = chatClient
                    .prompt()
                    .user(u -> u.text("""
                        Email: {email}
                        Classify the above email as IMPORTANT or NOT IMPORTANT. Let's
                        think step by step and explain why.
                        """)
                            .param("email", email))
                    .options(ChatOptions.builder()
                            .temperature(1.0)  // Higher temperature for more variation
                            .model("gemma3:4b-it-qat")
                            .build())
                    .call()
                    .entity(EmailClassification.class);

            // Count results
            if (output.classification() == EmailClassification.Classification.IMPORTANT) {
                importantCount++;
            } else {
                notImportantCount++;
            }
        }

        // Determine the final classification by majority vote
        String finalClassification = importantCount > notImportantCount ?
                "IMPORTANT" : "NOT IMPORTANT";

        System.out.println("Final jugdement:"+finalClassification);
    }

    @Test
    public void pt_tree_of_thoughts_game() {
        // Step 1: Generate multiple initial moves
        String initialMoves = chatClient
                .prompt("""
                    You are playing a game of chess. The board is in the starting position.
                    Generate 3 different possible opening moves. For each move:
                    1. Describe the move in algebraic notation
                    2. Explain the strategic thinking behind this move
                    3. Rate the move's strength from 1-10
                    """)
                .options(ChatOptions.builder()
                        .temperature(0.7)
                        .build())
                .call()
                .content();

        // Step 2: Evaluate and select the most promising move
        String bestMove = chatClient
                .prompt()
                .user(u -> u.text("""
                    Analyze these opening moves and select the strongest one:
                    {moves}

                    Explain your reasoning step by step, considering:
                    1. Position control
                    2. Development potential
                    3. Long-term strategic advantage

                    Then select the single best move.
                    """).param("moves", initialMoves))
                .call()
                .content();

        // Step 3: Explore future game states from the best move
        String gameProjection = chatClient
                .prompt()
                .user(u -> u.text("""
                    Based on this selected opening move:
                    {best_move}

                    Project the next 3 moves for both players. For each potential branch:
                    1. Describe the move and counter-move
                    2. Evaluate the resulting position
                    3. Identify the most promising continuation

                    Finally, determine the most advantageous sequence of moves.
                    """).param("best_move", bestMove))
                .call()
                .content();

        System.out.println("output: "+gameProjection);
    }

    @Test
    public void pt_automatic_prompt_engineering() {
        // Generate variants of the same request
        String orderVariants = chatClient
                .prompt("""
                    We have a band merchandise t-shirt webshop, and to train a
                    chatbot we need various ways to order: "One Metallica t-shirt
                    size S". Generate 10 variants, with the same semantics but keep
                    the same meaning.
                    """)
                .options(ChatOptions.builder()
                        .temperature(1.0)  // High temperature for creativity
                        .build())
                .call()
                .content();

        System.out.println("Generated Variants: "+orderVariants);

        // Evaluate and select the best variant
        String output = chatClient
                .prompt()
                .user(u -> u.text("""
                    Please perform BLEU (Bilingual Evaluation Understudy) evaluation on the following variants:
                    ----
                    {variants}
                    ----

                    Select the instruction candidate with the highest evaluation score.
                    """).param("variants", orderVariants))
                .call()
                .content();

        System.out.println("Output: "+output);
    }

    @Test
    void pt_code_prompting_writing_code() {
        String bashScript = chatClient
                .prompt("""
                    Write a code snippet in Bash, which asks for a folder name.
                    Then it takes the contents of the folder and renames all the
                    files inside by prepending the name draft to the file name.
                    """)
                .options(ChatOptions.builder()
                        .temperature(0.1)  // Low temperature for deterministic code
                        .build())
                .call()
                .content();

        System.out.println(bashScript);
    }

    @Test
    void pt_code_prompting_explaining_code() {
        String code = """
            #!/bin/bash
            echo "Enter the folder name: "
            read folder_name
            if [ ! -d "$folder_name" ]; then
            echo "Folder does not exist."
            exit 1
            fi
            files=( "$folder_name"/* )
            for file in "${files[@]}"; do
            new_file_name="draft_$(basename "$file")"
            mv "$file" "$new_file_name"
            done
            echo "Files renamed successfully."
            """;

        String explanation = chatClient
                .prompt()
                .user(u -> u.text("""
                    Explain to me the below Bash code:
                    ```
                    {code}
                    ```
                    """).param("code", code))
                .call()
                .content();

        System.out.println("Output: "+explanation);
    }

    @Test
    void pt_code_prompting_translating_code_1() {
        String bashCode = """
            #!/bin/bash
            echo "Enter the folder name: "
            read folder_name
            if [ ! -d "$folder_name" ]; then
            echo "Folder does not exist."
            exit 1
            fi
            files=( "$folder_name"/* )
            for file in "${files[@]}"; do
            new_file_name="draft_$(basename "$file")"
            mv "$file" "$new_file_name"
            done
            echo "Files renamed successfully."
            """;

        String pythonCode = chatClient
                .prompt()
                .user(u -> u.text("""
                    Translate the below Bash code to a Java 21 snippet:
                    Format the code in a readable way.
                    {code}
                    """).param("code", bashCode))
                .options(ChatOptions.builder()
                        .temperature(0.1) // Low temperature for deterministic code
                        .model("gemma3:4b-it-qat")
                        .build())
                .call()
                .content();
        System.out.println("Output: "+pythonCode);
    }

    @Test
    void pt_code_prompting_translating_code_2() {
        String bashCode = """
            #!/bin/bash
            echo "Enter the folder name: "
            read folder_name
            if [ ! -d "$folder_name" ]; then
            echo "Folder does not exist."
            exit 1
            fi
            files=( "$folder_name"/* )
            for file in "${files[@]}"; do
            new_file_name="draft_$(basename "$file")"
            mv "$file" "$new_file_name"
            done
            echo "Files renamed successfully."
            """;

        String pythonCode = chatClient
                .prompt()
                .user(u -> u.text("""
                    Translate the below Bash code to a Python snippet:
                    {code}
                    """).param("code", bashCode))
                .call()
                .content();
        System.out.println("Output: "+pythonCode);
    }

    @Test
    void pt_sql_prompting() {
        String prompt = """
            Generate a SQL query that groups the user by their country to see the population per country.
            Order the results by the population in descending order.
            Use the following table structure:
            
            User table column:
               id
               name
               birthDate
               
            Address table column:
               id
               userId
               country
          
            """;

        String sql = chatClient
                .prompt()
                .options(ChatOptions.builder()
                        .temperature(0.1) // Low temperature for deterministic code
                        .model("gemma3:4b-it-qat")
                        .build())
                .user(u -> u.text(prompt))
                .call()
                .content();
        System.out.println("Output: "+sql);
    }
}
