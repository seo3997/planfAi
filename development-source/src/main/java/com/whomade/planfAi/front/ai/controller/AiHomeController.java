package com.whomade.planfAi.front.ai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AiHomeController {
    @GetMapping("/front/ai")
    public String home() {
        return "front/ai/aiHome";
    }

    @GetMapping("/front/ai/chat-model")
    public String chatModel() {
        return "front/ai/chat-model";
    }

    @GetMapping("/front/ai/chat-model-stream")
    public String chatModelStream() {
        return "front/ai/chat-model-stream";
    }

}
