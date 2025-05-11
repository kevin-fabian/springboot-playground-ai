package com.example.springboot_ollama_toolcallback_api.assistant;

import com.example.springboot_ollama_toolcallback_api.services.AccountService;
import com.example.springboot_ollama_toolcallback_api.services.AccountTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EWalletAssistant {
    private final ChatClient client;
    private final AccountTool accountTool;
    private final ChatMemory chatMemory;

    public void ask(String userMessage){
        String response =  client.prompt()
                .user(userMessage)
                .system("""
							You are a customer chat agent support of the bank name Unite:
							Respond in a professional, helpful, and joyful manner. 
							You are interacting with customer through an online chat system.
							Check the message history first about this information before asking the user.
							Ask the account name first before doing any transactions.
							Use the provided function to get user account balance, withdraw from an account.
							Do not ask for any confirmation.
							The transaction is in real time.
							""")
//					.advisors(new SimpleLoggerAdvisor())
                .tools(accountTool)
                .advisors(new MessageChatMemoryAdvisor(chatMemory))
                .call()
                .content();
        log.info(response);
    }
}
