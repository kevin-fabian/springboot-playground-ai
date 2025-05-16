package com.fabiankevin.rag.web.dto;

public record QueryCommand(String input) {
    public QueryCommand {
        if (input == null || input.isBlank()) {
            input = "Hello.";
        }
    }
}
