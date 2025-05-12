# Spring AI: Ollama

This project contains multiple examples of using Spring AI with Ollama, a local LLM server. 
The examples demonstrate various use cases, prompt patterns, RAG (retrieval-augmented generation), Tool calling and other features of Spring AI.

## Common Commands Used
```bash
# To run the docker compose file

# To explicitly download specific AI model
docker exec -it ollama ollama run deepseek-r1:1.5b
docker exec -it ollama ollama run mistral
docker exec -it ollama ollama run gemma3:4b
docker exec -it ollama ollama run gemma3:4b-it-qat
docker exec -it ollama ollama run gemma3:1b-it-qat

## Embedding Model
docker exec -it ollama ollama pull nomic-embed-text

# To download embedding model
docker exec -it ollama ollama run mxbai-embed-large
```