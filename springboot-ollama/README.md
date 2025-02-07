# Spring AI: Ollama

This sample project covers the following
1. Downloading LLM and embedding models. 
2. Downloading PGVectorStore.
3. Reading a sample PDF to insert to the vectorStore to be used by Retrieval Augmenting Generation.
4. Communicating with the AI model using chat client with QuestionAnswerAdvisor.

## AI Model
This example uses `llama2` model

## Embedding Model
This example uses dedicated embedding model `mxbai-embed-large`

## Command that you might use 
```bash
# To run the docker compose file
docker compose up

# To explicitly download specific AI model
docker exec -it ollama ollama run mxbai-embed-large
docker exec -it ollama ollama run llama2
docker exec -it ollama ollama run deepseek-r1
```