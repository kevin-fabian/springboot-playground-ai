package com.fabiankevin.tools.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Description;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@Description("Tools for dealing with date and time")
@Slf4j
public class DateTimeTools {

    @Tool(description = "Get the current date and time in the user's timezone to answer date-related questions")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "Set a user alarm for the given time, provided in ISO-8601 format")
    public void setAlarm(@ToolParam(description = "Date and time in ISO-8601 format") String dateAndTime) {
        log.info("AI input: {}", dateAndTime);
        LocalDateTime alarmTime = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ISO_DATE_TIME);
        log.info("Alarm set for: {}", alarmTime);
    }
}