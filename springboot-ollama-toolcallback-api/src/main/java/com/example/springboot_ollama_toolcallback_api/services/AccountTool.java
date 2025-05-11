package com.example.springboot_ollama_toolcallback_api.services;

import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountTool {
    private final AccountService accountService;

    @Tool(description = "Retrieves the current balance of a user's account. Requires the accountName parameter, which must be obtained from the user beforehand. Returns the balance as a double value.")
    public double checkBalance(String accountName) {
        log.info("Checking balance for {}", accountName);
        return accountService.checkBalance(accountName);
    }

    @Tool(description = "Withdraws a specified amount from a user's account. Requires the accountName (obtained from the user) and withdrawAmount (the amount to withdraw as a Double). Processes the withdrawal through the account service.")
    public void withdraw(String accountName, Double withdrawAmount) {
        log.info("Withdrawing for {}", accountName);
        accountService.withdraw(accountName, withdrawAmount);
    }
}
