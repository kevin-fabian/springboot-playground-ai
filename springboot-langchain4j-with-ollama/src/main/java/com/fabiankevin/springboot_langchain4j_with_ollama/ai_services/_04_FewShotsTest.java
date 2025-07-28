package com.fabiankevin.springboot_langchain4j_with_ollama.ai_services;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.ArrayList;
import java.util.List;

import static com.fabiankevin.springboot_langchain4j_with_ollama.constant.OllamaAIModel.GEMMA3_1B_IT_QAT;

class _04_FewShotsTest {

    public static void main(String[] args) {
        ChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(GEMMA3_1B_IT_QAT)
                .temperature(0.7)
                .build();

        List<ChatMessage> fewShotHistory = new ArrayList<>();

        // Adding positive feedback example to history
        fewShotHistory.add(UserMessage.from(
                "I love the new update! The interface is very user-friendly and the new features are amazing!"));
        fewShotHistory.add(AiMessage.from(
                "Action: forward input to positive feedback storage\nReply: Thank you very much for this great feedback! We have transmitted your message to our product development team who will surely be very happy to hear this. We hope you continue enjoying using our product."));

        // Adding negative feedback example to history
        fewShotHistory.add(UserMessage
                .from("I am facing frequent crashes after the new update on my Android device."));
        fewShotHistory.add(AiMessage.from(
                "Action: open new ticket - crash after update Android\nReply: We are so sorry to hear about the issues you are facing. We have reported the problem to our development team and will make sure this issue is addressed as fast as possible. We will send you an email when the fix is done, and we are always at your service for any further assistance you may need."));

        // Adding another positive feedback example to history
        fewShotHistory.add(UserMessage
                .from("Your app has made my daily tasks so much easier! Kudos to the team!"));
        fewShotHistory.add(AiMessage.from(
                "Action: forward input to positive feedback storage\nReply: Thank you so much for your kind words! We are thrilled to hear that our app is making your daily tasks easier. Your feedback has been shared with our team. We hope you continue to enjoy using our app!"));

        // Adding another negative feedback example to history
        fewShotHistory.add(UserMessage
                .from("The new feature is not working as expected. It’s causing data loss."));
        fewShotHistory.add(AiMessage.from(
                "Action: open new ticket - data loss by new feature\nReply:We apologize for the inconvenience caused. Your feedback is crucial to us, and we have reported this issue to our technical team. They are working on it on priority. We will keep you updated on the progress and notify you once the issue is resolved. Thank you for your patience and support."));

        // Adding real user's message
        UserMessage customerComplaint = UserMessage
                .from("How can your app be so slow? Please do something about it!");
        fewShotHistory.add(customerComplaint);

        System.out.println("[User]: " + customerComplaint.singleText());
        System.out.print("[LLM]: ");

        String answer = model.chat(fewShotHistory).aiMessage().text();
        System.out.println(answer);
    }
}