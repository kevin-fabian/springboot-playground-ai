package com.fabiankevin.tools;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;

@SpringBootTest
class FunctionToolCallbackTest {
    @Autowired
    private ChatClient chatClient;

    @Test
    void test() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("currentWeather", new WeatherService())
                .description("Get the weather in location")
                .inputType(WeatherRequest.class)
                .build();

        String content = chatClient.prompt("What is the weather in Baguio city?")
                .tools(toolCallback)
                .call()
                .content();

        System.out.println("Result: "+content);
    }

}


@Slf4j
class WeatherService implements Function<WeatherRequest, WeatherResponse> {
    public WeatherResponse apply(WeatherRequest request) {
        log.info("WeatherRequest: {}", request);
        return new WeatherResponse(30.0, Unit.C);
    }
}

enum Unit {C, F}
record WeatherRequest(String location, Unit unit) {}
record WeatherResponse(double temp, Unit unit) {}