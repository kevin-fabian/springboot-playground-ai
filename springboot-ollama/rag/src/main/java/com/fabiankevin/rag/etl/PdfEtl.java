package com.fabiankevin.rag.etl;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Slf4j
@Profile("!test")
public class PdfEtl {

    @Value("${vector-store.file-store}")
    private String vectorStoreFile;

    @Value("classpath:spring_ai_prompt_engineering_patterns.pdf")
    private Resource document;

    @Autowired
    private SimpleVectorStore vectorStore;

    @PostConstruct
    public void loadAndInsert() {
        log.info("Loading and inserting documents into the vector store...");
        File vectoreStoreFile = new File(this.vectorStoreFile);
        if (vectoreStoreFile.exists()) {
            log.info("Loading vector store from {}", vectoreStoreFile.getName());
            vectorStore.load(vectoreStoreFile);
        } else {
            log.info("Loading document: {}", document.getFilename());
            TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(document);

            List<Document> documents1 = tikaDocumentReader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> splitDocs = tokenTextSplitter.apply(documents1);

            vectorStore.add(splitDocs);
            vectorStore.save(vectoreStoreFile);
        }
    }
}
