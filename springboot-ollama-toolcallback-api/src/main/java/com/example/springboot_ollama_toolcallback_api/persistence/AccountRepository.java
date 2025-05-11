package com.example.springboot_ollama_toolcallback_api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByName(String name);
}
