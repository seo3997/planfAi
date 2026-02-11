package com.whomade.planfAi.front.ai.service;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AiService {

    @Autowired
    private ChatModel chatModel;

    private final String SYSTEM_INSTRUCTION = "사용자 질문에 대해 한국어로 답변을 해야 합니다.";

    public String generateText(String question) {
        // 1. 메시지 생성 (builder() 대신 생성자 사용)
        SystemMessage systemMessage = new SystemMessage(SYSTEM_INSTRUCTION);
        UserMessage userMessage = new UserMessage(question);

        // 2. 옵션 설정 (OpenAiChatOptions를 사용하면 더 구체적인 설정 가능)
        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .model("gpt-4o")
                .temperature(0.3)
                .maxTokens(1000)
                .build();

        // 3. 프롬프트 생성
        Prompt prompt = new Prompt(java.util.List.of(systemMessage, userMessage), chatOptions);

        // 4. 호출 및 응답 텍스트 추출
        ChatResponse chatResponse = chatModel.call(prompt);
        return chatResponse.getResult().getOutput().getText();
    }

    public Flux<String> generateStreamText(String question) {
        SystemMessage systemMessage = new SystemMessage(SYSTEM_INSTRUCTION);
        UserMessage userMessage = new UserMessage(question);

        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .model("gpt-4o")
                .temperature(0.3)
                .maxTokens(1000)
                .build();


        Prompt prompt = new Prompt(java.util.List.of(systemMessage, userMessage), chatOptions);

        // 스트림 응답 처리
        return chatModel.stream(prompt)
                .map(response -> {
                    String content = response.getResult().getOutput().getText();
                    return (content != null) ? content : "";
                });
    }
}