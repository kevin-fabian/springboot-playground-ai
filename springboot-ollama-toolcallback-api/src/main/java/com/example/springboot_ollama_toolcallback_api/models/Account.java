package com.example.springboot_ollama_toolcallback_api.models;

import com.example.springboot_ollama_toolcallback_api.exception.InsufficientBalance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private UUID id;
    private String name;
    private double amount;

    public void withdraw(Double amountToWithdraw) {
        if( amount >= amountToWithdraw ){
            amount = amount - amountToWithdraw;
        } else {
            throw new InsufficientBalance();
        }

    }
}
