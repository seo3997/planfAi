package com.whomade.planfAi.front.survey.controller;

import com.whomade.planfAi.admin.mgt.survey.service.SurveyService;
import com.whomade.planfAi.admin.mgt.survey.vo.SurveyResponseDto;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSurvey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/survey")
@RequiredArgsConstructor
public class PublicSurveyController {

    private final SurveyService surveyService;

    @Value("${survey.domain}")
    private String surveyDomain;

    /**
     * 설문 URL 생성 예시
     */
    @GetMapping("/generate-url/{surveyId}")
    @ResponseBody
    public String getSurveyUrl(@PathVariable String surveyId) {
        String prefix = surveyDomain.startsWith("http") ? "" : "https://";
        return prefix + surveyDomain + "/survey/v/" + surveyId;
    }

    /**
     * 설문 응답 페이지 (공개용)
     */
    @GetMapping("/v/{surveyId}")
    public String viewPublicSurvey(@PathVariable String surveyId, Model model) {
        TbSurvey searchVO = new TbSurvey();
        searchVO.setSurveyId(surveyId);

        TbSurvey survey = surveyService.selectSurveyEntryForm(searchVO);

        // 상태 체크 로직 (Interceptor로 처리하는 것이 권장되나 컨트롤러에서도 기본 체크)
        if (survey == null || !"OPENED".equals(survey.getOpened())) {
            return "front/survey/error/not_available"; // 접근 불가 페이지
        }

        model.addAttribute("survey", survey);
        model.addAttribute("surveyForm", survey); // For compatibility with existing preview templates if reused

        return "front/survey/surveyView";
    }

    @GetMapping("/error/not_available")
    public String notAvailable() {
        return "front/survey/error/not_available";
    }

    /**
     * 설문 응답 제출 API
     */
    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitSurvey(@RequestBody SurveyResponseDto responseDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            surveyService.saveSurveyResponse(responseDto);
            result.put("status", "success");
            result.put("message", "설문에 응답해 주셔서 감사합니다.");
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @Value("${file.board.upload-dir}")
    private String uploadDir;

    @Value("${file.board.public-url}")
    private String publicUrl;

    /**
     * 이미지 업로드 API
     */
    @PostMapping("/upload-image")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (file.isEmpty()) {
                throw new Exception("파일이 비어있습니다.");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String savedName = java.util.UUID.randomUUID().toString() + extension;

            // 날짜별 폴더 생성
            String datePath = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date());
            java.io.File dir = new java.io.File(uploadDir + "/survey/" + datePath);
            if (!dir.exists())
                dir.mkdirs();

            java.io.File dest = new java.io.File(dir, savedName);
            file.transferTo(dest);

            result.put("status", "success");
            result.put("filePath", "/common/img/board/survey/" + datePath + "/" + savedName);
            result.put("fullUrl", publicUrl + "survey/" + datePath + "/" + savedName);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
