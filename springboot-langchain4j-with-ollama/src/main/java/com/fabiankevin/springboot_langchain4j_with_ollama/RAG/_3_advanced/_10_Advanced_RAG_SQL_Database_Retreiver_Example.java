package com.fabiankevin.springboot_langchain4j_with_ollama.RAG._3_advanced;

import com.fabiankevin.springboot_langchain4j_with_ollama.RAG.Assistant;
import com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel;
import dev.langchain4j.experimental.rag.content.retriever.sql.SqlDatabaseContentRetriever;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.fabiankevin.springboot_langchain4j_with_ollama.RAG.Utils.startConversationWith;
import static com.fabiankevin.springboot_langchain4j_with_ollama.RAG.Utils.toPath;


public class _10_Advanced_RAG_SQL_Database_Retreiver_Example {
    private static final OllamaChatModel model = OllamaChatModel.builder()
            .baseUrl("http://localhost:11434")
            .modelName(OllamaAIModel.GEMMA3_4B_IT_QAT)
            .temperature(0.0)
            .build();

    /**
     * <p>
     * Advanced RAG in LangChain4j is described here: https://github.com/langchain4j/langchain4j/pull/538
     * <p>
     * This example demonstrates how to use SQL database content retriever.
     * <p>
     * WARNING! Although fun and exciting, {@link SqlDatabaseContentRetriever} is dangerous to use!
     * Do not ever use it in production! The database user must have very limited READ-ONLY permissions!
     * Although the generated SQL is somewhat validated (to ensure that the SQL is a SELECT statement),
     * there is no guarantee that it is harmless. Use it at your own risk!
     * <p>
     * In this example we will use an in-memory H2 database with 3 tables: customers, products and orders.
     * See "resources/sql" directory for more details.
     * <p>
     * This example requires "langchain4j-experimental-sql" dependency.
     */

    public static void main(String[] args) {

        Assistant assistant = createAssistant();

        // You can ask questions such as "How many customers do we have?" and "What is our top seller?".
        startConversationWith(assistant);
    }

    private static Assistant createAssistant() {

        DataSource dataSource = createDataSource();


        ContentRetriever contentRetriever = SqlDatabaseContentRetriever.builder()
                .dataSource(dataSource)
                .chatModel(model)
                .build();

        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .contentRetriever(contentRetriever)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    private static DataSource createDataSource() {

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");

        String createTablesScript = read("sql/create_tables.sql");
        execute(createTablesScript, dataSource);

        String prefillTablesScript = read("sql/prefill_tables.sql");
        execute(prefillTablesScript, dataSource);

        return dataSource;
    }

    private static String read(String path) {
        try {
            return new String(Files.readAllBytes(toPath(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void execute(String sql, DataSource dataSource) {
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            for (String sqlStatement : sql.split(";")) {
                statement.execute(sqlStatement.trim());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}