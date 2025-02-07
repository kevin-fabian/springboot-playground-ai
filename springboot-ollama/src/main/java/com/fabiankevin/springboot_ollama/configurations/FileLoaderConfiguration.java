package com.fabiankevin.springboot_ollama.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileLoaderConfiguration {

    @Autowired
    private VectorStore vectorStore;

//    @Bean
//    public CommandLineRunner jsonEtl(@Value("classpath:bikes.json") Resource resource) {
//        return args -> {
//            log.info("Reading {}", resource.getFile());
//            JsonReader jsonReader = new JsonReader(resource, "description", "content");
//            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
//            List<Document> documents = tokenTextSplitter.apply(jsonReader.get());
//            vectorStore.accept(documents);
//            log.info("Done inserting.");
//        };
//    }

//    @Bean
//    public CommandLineRunner applicationRunner(@Value("${documents.directory.path}") String directoryPath) {
//        return args -> {
//            String absolutePath = Paths.get(directoryPath).toFile().getAbsolutePath();
//            log.info("File directory path: {}", absolutePath);
//
//            try (Stream<Path> paths = Files.list(Paths.get(absolutePath))) {
//                paths.filter(Files::isRegularFile)
//                        .forEach(path -> {
//                            log.info("Reading: {}", path);
//                            try {
//                                UrlResource urlResource = new UrlResource(path.toUri());
//
//
//                                PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(urlResource,
//                                        PdfDocumentReaderConfig.builder()
//                                                .withPageTopMargin(0)
//                                                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
//                                                        .withNumberOfTopTextLinesToDelete(0)
//                                                        .build())
//                                                .withPagesPerDocument(1)
//                                                .build());
//
//                                log.info("Transforming: {}", path);
//                                TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
//                                List<Document> documents = tokenTextSplitter.apply(pdfReader.read());
//                                log.info("Storing {} to the Vector database", path.getFileName());
//                                Instant now = Instant.now();
//                                vectorStore.accept(documents);
//                                log.info("Done. Execution Time: {}", Duration.between(Instant.now(), now));
//                            } catch (MalformedURLException e) {
//                                throw new RuntimeException(e);
//                            }
//                        });
//            }
//        };
//    }
}
