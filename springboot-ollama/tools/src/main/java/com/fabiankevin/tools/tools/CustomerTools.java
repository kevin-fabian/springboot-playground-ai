package com.fabiankevin.tools.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class CustomerTools {
    private final Map<Long, Customer> customerMap = Map.of(
            1L, new Customer(1L, "John Doe", LocalDate.of(1992, 1, 1)),
            5L, new Customer(5L, "Foo D. Dragon", LocalDate.of(2000, 8, 15))
    );

    @Tool(description = "Get a customer by ID")
    public Customer getCustomer(@ToolParam(description = "Customer id to be retrieved") Long id, ToolContext context) {
        log.info("User id={} tenantId={}", id, context.getContext().get("tenantId"));
        return Optional.ofNullable(customerMap.get(id))
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }
}

record Customer(Long id, String name, LocalDate birthday) {}
