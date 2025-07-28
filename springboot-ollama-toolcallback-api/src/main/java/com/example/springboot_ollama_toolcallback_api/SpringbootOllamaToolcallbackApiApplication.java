package com.example.springboot_ollama_toolcallback_api;

import com.example.springboot_ollama_toolcallback_api.assistant.EWalletAssistant;
import com.example.springboot_ollama_toolcallback_api.persistence.AccountEntity;
import com.example.springboot_ollama_toolcallback_api.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class SpringbootOllamaToolcallbackApiApplication {
	private final EWalletAssistant eWalletAssistant;
	CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {
		SpringApplication.run(SpringbootOllamaToolcallbackApiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AccountRepository accountRepository){
		return args -> {
			AccountEntity accountEntity = new AccountEntity();
			accountEntity.setCreatedDate(Instant.now());
			accountEntity.setName("Sandro");
			accountEntity.setMoney(10_000.00);
			accountRepository.save(accountEntity);

			Instant now = Instant.now();
			log.info("============ \r\r");
			eWalletAssistant.ask("Hello, tell me what can you do.");
			latch.await(2, TimeUnit.SECONDS);
			eWalletAssistant.ask("I want to check my current balance. My account name is Sandro.");
			latch.await(2, TimeUnit.SECONDS);
			eWalletAssistant.ask("I want to withdraw 5000 in my account also show me my account balance after the withdrawal.");
//			latch.await(2, TimeUnit.SECONDS);
//			eWalletAssistant.ask("I would like to withdraw 1000 from my account.");
//			latch.await(2, TimeUnit.SECONDS);
//			eWalletAssistant.ask("My account name is Sandro. Could you check my balance again?");

//			log.info("{}", response);
			log.info("Execution time: {}", Duration.between(Instant.now(), now));

		};
	}


}
