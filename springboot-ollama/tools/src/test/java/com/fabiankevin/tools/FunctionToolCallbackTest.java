package com.fabiankevin.tools;

import com.fabiankevin.tools.services.WeatherService;
import com.fabiankevin.tools.services.dto.WeatherRequest;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class FunctionToolCallbackTest {
    @Autowired
    private ChatClient chatClient;
    @Autowired
    @MockitoSpyBean
    private WeatherService weatherService;

    @Test
    void shouldCallWeatherFunction() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("currentWeather", weatherService)
                .description("Get the weather in location")
                .inputType(WeatherRequest.class)
                .build();

        String content = chatClient.prompt("What is the weather in Baguio city?")
                .tools(toolCallback)
                .call()
                .content();

        System.out.println("Result: "+content);
        verify(weatherService, times(1)).apply(any());
    }
}
