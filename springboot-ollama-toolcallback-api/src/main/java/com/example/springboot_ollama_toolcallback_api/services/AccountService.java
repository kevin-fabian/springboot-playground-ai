package com.example.springboot_ollama_toolcallback_api.services;

import com.example.springboot_ollama_toolcallback_api.models.Account;
import com.example.springboot_ollama_toolcallback_api.persistence.AccountEntity;
import com.example.springboot_ollama_toolcallback_api.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    public double checkBalance(String accountName) {
        return accountRepository.findByName(accountName)
                .map(AccountEntity::getMoney)
                .orElse(0.0);
    }

    public void withdraw(String accountName, Double amountToWithdraw){
        AccountEntity accountEntity = accountRepository.findByName(accountName).get();
        Account account = new Account();
        account.setId(accountEntity.getId());
        account.setName(accountEntity.getName());
        account.setAmount(account.getAmount());

        account.withdraw(amountToWithdraw);

        log.info("Account after withdrawal: {}", account);
        accountRepository.save(accountEntity);
    }


}
