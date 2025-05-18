package com.fabiankevin.tools;

import com.fabiankevin.tools.tools.CustomerTools;
import com.fabiankevin.tools.tools.DateTimeTools;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Slf4j
class CustomerToolsCallingTest {
    @Autowired
    private ChatClient chatClient;

    @MockitoSpyBean
    @Autowired
    private CustomerTools customerTools;


    @Test
    void shouldCallCustomerTools() {

        String result = chatClient.prompt("Give me the info about customer 5")
                .tools(customerTools)
                .toolContext(Map.of("tenantId", 100L))
                .call()
                .content();

        log.info("Result: {}", result);
        verify(customerTools, times(1)).getCustomer(any(Long.class), any());
    }




}
