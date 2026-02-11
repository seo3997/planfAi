package com.whomade.planfAi.front.ai.controller;

import com.whomade.planfAi.front.ai.service.AiService;
import com.whomade.planfAi.front.ai.service.AiServiceByChatClient;
import com.whomade.planfAi.front.profitReport.vo.ProfitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.List;
import com.whomade.planfAi.front.profitReport.mapper.ProfitReportMapper;

@RestController
@RequestMapping("/api/ai")
@Slf4j
@RequiredArgsConstructor
public class AiController {
    // ##### 필드 #####
    // @Autowired
    // private AiService aiService;

    @Autowired
    private AiServiceByChatClient aiService;

    private final ProfitReportMapper profitMapper; // MyBatis Mapper 주입

    // ##### 요청 매핑 메소드 #####
    @PostMapping(value = "/chat-model", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String chatModel(@RequestParam("question") String question) {
        String answerText = aiService.generateText(question);
        return answerText;
    }

    @PostMapping(value = "/chat-model-stream", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> chatModelStream(@RequestParam("id") Long reportId) {
        // 1. 리포트 ID로 조회
        ProfitVo report = profitMapper.selectReportById(reportId);

        // 2. 조회된 데이터가 있는지 검증
        if (report == null) {
            log.warn("리포트 ID {}에 대한 데이터가 존재하지 않습니다.", reportId);
            return Flux.just("분석할 리포트 데이터가 없습니다. 유효한 리포트인지 확인해주세요.");
        }

        // 3. 리포트에서 JSON 추출
        String reportData = report.getReportData();

        // 4. AI 분석 서비스 호출 (스트리밍)
        //return aiService.analyzeReport(reportData);
        return aiService.analyzeReportDeepDive(reportData);
    }
}
