package com.fabiankevin.tools;

import com.fabiankevin.tools.tools.DateTimeTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.DefaultChatOptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ToolsCallingTest {
    @Autowired
    private ChatClient chatClient;

    @MockitoSpyBean
    @Autowired
    private DateTimeTools dateTimeTools;


    @Test
    void whenAskAboutDateTime_thenShouldInvokeGetCurrentDateTimeMethod() {
        System.out.println(LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString());

        String result = chatClient.prompt("What day is tomorrow?")
                .tools(dateTimeTools)
                .system("Response with answer only.")
                .call()
                .content();
        System.out.println(result);
//
        verify(dateTimeTools, times(1)).getCurrentDateTime();
    }

    @Test
    void shouldSetAlarmTenMinutesFromNow() {
        String result = chatClient.prompt("""
                        Can you set an alarm for 30 minutes from now.
                        """)
                .system("""
                        Determine what day and time at the moment is in the user's timezone.

                        Here are is the example scenario.
                        
                        Scenario 1:
                        Given the current date time is: 2025-05-18T11:18:50.00+08:00
                        Question: Set an alarm for 10 minutes from now.
                        Answer: 2025-05-18T21:18:50.00+08:00
                        
                        Scenario 2:
                        Given the current date time is: 2025-05-18T11:18:50.00+08:00
                        Question: Set an alarm for 30 minutes from now.
                        Answer: 2025-05-18T41:18:50.00+08:00
                        
                        Let's think step by step.
                        """)
                .tools(dateTimeTools)
                .call()
                .content();
        System.out.println(result);

        verify(dateTimeTools, times(1)).getCurrentDateTime();
        verify(dateTimeTools, times(1)).setAlarm(any());
    }

}
