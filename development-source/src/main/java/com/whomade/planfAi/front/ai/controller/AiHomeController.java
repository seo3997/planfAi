package com.whomade.planfAi.front.ai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AiHomeController {
  @GetMapping("/front/ai")
  public String home() {
    return "front/ai/aiHome";
  }
}
