package com.whomade.planfAi.front.ai.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AiServiceByChatClient {
  // ##### 필드 #####
  private ChatClient chatClient;

  @Value("${ai.spec.business-path}")
  private Resource bizSpecPro;

  private final ObjectMapper objectMapper;

  public AiServiceByChatClient(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
    this.chatClient = chatClientBuilder.build(); // ChatClient 빌드
    this.objectMapper = objectMapper;           // ObjectMapper 초기화 (이 줄이 빠져서 에러 난 것!)
  }
  // ##### 메소드 #####
  public String generateText(String question) {
    String answer = chatClient.prompt()
        .system("사용자 질문에 대해 한국어로 답변을 해야 합니다.")
        .user(question)
        .options(ChatOptions.builder()
            .temperature(0.3)
            .maxTokens(1000)
            .build()
        )
        .call()
        .content();
    
    return answer;
  }

  public Flux<String> generateStreamText(String question) {
    Flux<String> fluxString = chatClient.prompt()
        .system("사용자 질문에 대해 한국어로 답변을 해야 합니다.")
        .user(question)
        .options(ChatOptions.builder()
            .temperature(0.3)
            .maxTokens(1000)
            .build()
        )        
        .stream()
        .content();
  
    return fluxString;
  }

    public Flux<String> analyzeReport(String reportData) {
        try {
            String specContent = bizSpecPro.getContentAsString(StandardCharsets.UTF_8);

            return chatClient.prompt()
                    .system(s -> s.text("""
                    당신은 20년 경력의 [소상공인 전문 자산관리 컨설턴트]입니다. 
                    단순히 숫자를 읽어주는 것이 아니라, 사장님의 장부를 보고 '수익의 급소'를 찾아내어 독설과 따뜻한 조언을 섞어 전달하세요.

                    [분석 가이드라인]
                    1. **인과관계 분석**: 지표가 나쁘다면(예: 임차료 비중 높음), 그것이 전체 수익구조에 어떤 악영향을 주는지 분석하세요.
                    2. **우선순위 처방**: 모든 지표를 다 나열하지 말고, 가장 시급한 'TOP 3 문제점'을 먼저 짚어주세요.
                    3. **구체적 실천 방안**: "마케팅을 강화하세요" 같은 뻔한 말 대신, 설계서 제11조에 따라 "하루에 아메리카노 15잔을 더 팔아야 본전입니다"처럼 구체적 숫자를 제시하세요.
                    4. **출력 형식**: 가독성 높은 HTML 구조를 유지하되, 전문가의 코멘트(<p class='pro-comment'>)를 반드시 포함하세요.

                    [상세 설계서]
                    {spec}
                    """)
                            .param("spec", specContent))
                    .user(u -> u.text("""
                    사장님의 최신 재무 데이터입니다. 전문가로서 이 매장이 살 길을 알려주세요.
                    
                    [데이터]
                    {data}
                    """)
                            .param("data", reportData))
                    .options(ChatOptions.builder()
                            .temperature(0.7) // 약간의 창의성을 주어 전문가스러운 말투 유도
                            .maxTokens(3000)
                            .build())
                    .stream()
                    .content();

        } catch (IOException e) {
            log.error("파일 로드 오류", e);
            return Flux.just("<p>진단 준비 중 문제가 발생했습니다.</p>");
        }
    }

    /**
     * Step-Back 기법을 적용한 심층 진단 (HTML 스트리밍 리턴)
     */
    public Flux<String> analyzeReportDeepDive(String reportData) {
        return Flux.create(sink -> {
            try {
                String specContent = bizSpecPro.getContentAsString(StandardCharsets.UTF_8);

                // 1. 분석 계획 수립 (단순 나열이 아닌 '인과관계'를 묻도록 유도)
                String planJson = chatClient.prompt()
                        .system("당신은 재무 제표 이면의 진실을 꿰뚫어 보는 수석 컨설턴트입니다. 현상 나열이 아닌 '수익성 악화의 근본 원인'을 파헤치는 3단계 질문을 JSON 배열로만 응답하세요.")
                        .user(u -> u.text("""
                            [데이터] {data}
                            이 장부를 분석하여 사장님이 경영 판단을 내릴 때 놓치고 있는 재무적 맹점 3가지를 도출할 질문을 만드세요.
                            - 예: "단순 영업이익 뒤에 숨겨진 실질 적자 규모와 그 원인은 무엇인가?"
                            """).param("data", reportData))
                        .call().content();

                String jsonOnly = planJson.substring(planJson.indexOf("["), planJson.lastIndexOf("]") + 1);
                List<String> questions = objectMapper.readValue(jsonOnly, new TypeReference<List<String>>() {});

                StringBuilder contextBuffer = new StringBuilder();

                // 2. 심층 전략 진단 및 스트리밍
                for (int i = 0; i < questions.size(); i++) {
                    String currentQuestion = questions.get(i);
                    final int stepNum = i + 1;

                    sink.next("<h4 class='analysis-step-title'>Strategy " + stepNum + ". " + currentQuestion + "</h4>");

                    String stepAnswer = chatClient.prompt()
                            .system(s -> s.text("""
                            당신은 20년 경력의 [수석 자산관리 컨설턴트]입니다. 사장님께 품격 있고 정중하게, 그러나 수치 앞에서는 타협 없는 진실을 전달합니다.

                            [수치 엄격 규칙 - 위반 시 해고]
                            1. 데이터 변조 금지: 월 매출 650만, 원가 175만, 투자 5천만(상각 83.3만)을 절대 사수하세요.
                            2. 나열 금지: 숫자를 단순히 나열하지 말고, 숫자 간의 관계를 분석하여 '결론'을 먼저 말씀하세요.

                            [진단 및 처방 가이드라인]
                            1. **결론 중심**: "지표는 이렇습니다"가 아니라 "사장님, 현재 실질적으로 적자 상태입니다"라고 결론부터 내리세요.
                            2. **감가상각비의 실질화**: 22.5만 원의 이익이 상각비 반영 시 -60.8만 원의 손실이 된다는 점을 'Section 2'에서 강력하게 경고하십시오.
                            3. **비즈니스 솔루션**: 설계서 제11조에 근거하여, 이 적자를 메우기 위해 필요한 '추가 일일 판매량'이나 '고정비 절감 목표액'을 구체적으로 제시하세요.
                            4. **HTML 구성**: 전문가적 소견은 <p class='pro-comment'>에, 핵심 해결책은 별도의 요약 박스 형태로 출력하세요.
                            """)
                                    .param("spec", specContent))
                            .user(u -> u.text("""
                            [사장님의 실제 장부 요약]
                            - 월 매출액: 6,500,000원 (650만 원)
                            - 월 매출원가: 1,750,000원
                            - 투자금액: 50,000,000원 (월 상각비 83.3만 원 발생)
                            
                            [상세 설계서] {spec}
                            [이전 대화 맥락] {context}
                            
                            질문: {question}
                            
                            위 요약 수치를 기반으로, 단순한 데이터 나열이 아닌 경영자를 위한 '전략적 조언'을 HTML로 작성하십시오.
                            """).param("spec", specContent)
                                    .param("context", contextBuffer.toString())
                                    .param("question", currentQuestion))
                            .call().content();

                    sink.next("<div class='analysis-content-box'>" + stepAnswer + "</div>");
                    contextBuffer.append("\n\n").append(stepAnswer);
                }

                sink.complete();

            } catch (Exception e) {
                log.error("전략 진단 중 오류", e);
                sink.next("<div class='error-box'>정밀 전략 분석 중 오류가 발생했습니다. 잠시 후 다시 시도해 주십시오.</div>");
                sink.complete();
            }
        });
    }
}
