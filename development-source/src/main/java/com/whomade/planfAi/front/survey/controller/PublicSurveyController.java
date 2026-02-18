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

    @Value("${file.survey.upload-dir}")
    private String surveyUploadDir;

    @Value("${file.survey.public-url}")
    private String surveyPublicUrl;

    /**
     * 이미지 업로드 API
     */
    @PostMapping("/upload-image")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(value = "surveyId", required = false) String surveyId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (file.isEmpty()) {
                throw new Exception("파일이 비어있습니다.");
            }

            String targetId = (surveyId == null || surveyId.isEmpty()) ? "common" : surveyId;
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String savedName = java.util.UUID.randomUUID().toString() + extension;

            // 저장 디렉토리 설정 (trailing slash 제거 후 조합)
            String baseDir = surveyUploadDir.endsWith("/") ? surveyUploadDir.substring(0, surveyUploadDir.length() - 1)
                    : surveyUploadDir;
            java.io.File dir = new java.io.File(baseDir + "/" + targetId);
            if (!dir.exists())
                dir.mkdirs();

            java.io.File dest = new java.io.File(dir, savedName);
            file.transferTo(dest);

            // 공용 URL 생성 (trailing slash 체크)
            String baseUrl = surveyPublicUrl.endsWith("/") ? surveyPublicUrl : surveyPublicUrl + "/";
            String finalUrl = baseUrl + targetId + "/" + savedName;

            result.put("status", "success");
            result.put("filePath", finalUrl);
            result.put("fullUrl", finalUrl);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
