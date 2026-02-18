package com.whomade.planfAi.admin.mgt.survey.controller;

import com.whomade.planfAi.admin.mgt.survey.service.SurveyService;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSurvey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/mgt/survey")
@RequiredArgsConstructor
@Slf4j
public class SurveyController {

    private final SurveyService surveyService;

    @org.springframework.beans.factory.annotation.Value("${app.domain}")
    private String surveyDomain;

    /**
     * 설문 목록 조회 (00.설문리스트관리_01~02.png 대응)
     * URL: /mgt/survey/selectPageListSurvey.do
     */
    @GetMapping("/selectPageListSurvey.do")
    public String selectPageListSurvey(@ModelAttribute("searchVO") TbSurvey searchVO, Model model) {

        // 1. 페이징 설정 (기본값 처리)
        if (searchVO.getPageIndex() < 1) {
            searchVO.setPageIndex(1);
        }

        // 2. 페이징 계산
        int pageIndex = searchVO.getPageIndex();
        int pageSize = searchVO.getRecordCountPerPage();
        // MySQL LIMIT offset calculation: (page - 1) * size.
        // If page 1, offset 0.
        // TbSurvey default firstIndex is 1, which causes LIMIT 1, 10 (skipping 1st
        // row).
        // So we must manually set firstIndex based on pageIndex.
        searchVO.setFirstIndex((pageIndex - 1) * pageSize);

        // 3. 목록 조회
        List<TbSurvey> resultList = surveyService.selectSurveyList(searchVO);
        int totalCount = surveyService.selectSurveyCount(searchVO);

        // 3. 모델에 담기
        model.addAttribute("resultList", resultList);
        model.addAttribute("totalCount", totalCount);

        return "admin/mgt/survey/surveyList";
    }

    /**
     * 설문 마스터 관리 (01.설문마스타관리.png 대응)
     * URL: /mgt/survey/manageSurveyMenu.do
     */
    @GetMapping("/manageSurveyMenu.do")
    public String manageSurveyMenu(@ModelAttribute("searchVO") TbSurvey searchVO, Model model) {
        TbSurvey surveyInfo = surveyService.selectSurveyDetail(searchVO);
        model.addAttribute("surveyInfo", surveyInfo);
        model.addAttribute("surveyDomain", surveyDomain);

        // 테마 목록 추가
        model.addAttribute("themeList", surveyService.selectThemeList());

        return "admin/mgt/survey/surveyMaster";
    }

    /**
     * 설문 마스터 정보 수정
     * URL: /mgt/survey/updateSurveyMaster.do
     */
    @org.springframework.web.bind.annotation.PostMapping("/updateSurveyMaster.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> updateSurveyMaster(
            @org.springframework.web.bind.annotation.RequestBody TbSurvey surveyVO) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            surveyService.updateSurvey(surveyVO);
            result.put("status", "success");
            result.put("message", "저장되었습니다.");
        } catch (Exception e) {
            log.error("Survey Master Update Error", e);
            result.put("status", "error");
            result.put("message", "저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    /**
     * 설문 양식 등록 (02.설문양식등록.png 대응)
     * URL: /mgt/survey/editEntryForm.do
     */
    @GetMapping("/editEntryForm.do")
    public String editEntryForm(@ModelAttribute("searchVO") TbSurvey searchVO, Model model) {
        TbSurvey surveyForm = null;

        // ID가 있는 경우 상세 데이터 조회 (수정 모드)
        if (searchVO.getSurveyId() != null && !searchVO.getSurveyId().isEmpty()) {
            surveyForm = surveyService.selectSurveyEntryForm(searchVO);
        }

        // 신규 등록 모드인 경우 빈 객체 생성 또는 처리
        if (surveyForm == null) {
            surveyForm = new TbSurvey();
            // 기본값 설정 등이 필요하면 여기서 처리
        }

        model.addAttribute("surveyForm", surveyForm);
        return "admin/mgt/survey/entryForm";
    }

    /**
     * 설문 양식 저장
     * URL: /mgt/survey/saveEntryForm.do
     */
    @org.springframework.web.bind.annotation.PostMapping("/saveEntryForm.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> saveEntryForm(
            @org.springframework.web.bind.annotation.RequestBody TbSurvey surveyVO) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            surveyService.saveSurveyEntryForm(surveyVO);
            result.put("status", "success");
            result.put("message", "저장되었습니다.");
            result.put("surveyId", surveyVO.getSurveyId());
        } catch (Exception e) {
            log.error("Survey Save Error", e);
            result.put("status", "error");
            result.put("message", "저장 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    /**
     * 설문 미리보기 (Admin Preview)
     * URL: /mgt/survey/preview.do
     */
    @GetMapping("/preview.do")
    public String preview(@ModelAttribute("searchVO") TbSurvey searchVO, Model model) {
        TbSurvey surveyForm = null;
        if (searchVO.getSurveyId() != null && !searchVO.getSurveyId().isEmpty()) {
            surveyForm = surveyService.selectSurveyEntryForm(searchVO);
        }

        if (surveyForm == null) {
            // Handle error or redirect
            return "redirect:/mgt/survey/selectPageListSurvey.do";
        }

        model.addAttribute("surveyForm", surveyForm);
        return "admin/mgt/survey/preview";
    }

    /**
     * 설문 삭제
     */
    @org.springframework.web.bind.annotation.PostMapping("/deleteSurvey.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> deleteSurvey(
            @org.springframework.web.bind.annotation.RequestBody TbSurvey surveyVO) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            surveyService.deleteSurvey(surveyVO);
            result.put("status", "success");
            result.put("message", "삭제되었습니다.");
        } catch (Exception e) {
            log.error("Survey Delete Error", e);
            result.put("status", "error");
            result.put("message", "삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    /**
     * 설문 복사
     */
    @org.springframework.web.bind.annotation.PostMapping("/copySurvey.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> copySurvey(
            @org.springframework.web.bind.annotation.RequestBody TbSurvey surveyVO) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        try {
            String newId = surveyService.copySurvey(surveyVO.getSurveyId());
            result.put("status", "success");
            result.put("message", "복사되었습니다.");
            result.put("surveyId", newId);
        } catch (Exception e) {
            log.error("Survey Copy Error", e);
            result.put("status", "error");
            result.put("message", "복사 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    /**
     * 설문 통계/응답 결과 (03.설문통계.png 대응)
     * URL: /mgt/survey/surveyStats.do
     */
    @GetMapping("/surveyStats.do")
    public String surveyStats(@ModelAttribute("searchVO") TbSurvey searchVO, Model model) {
        TbSurvey surveyForm = surveyService.selectSurveyEntryForm(searchVO);
        if (surveyForm == null) {
            return "redirect:/mgt/survey/selectPageListSurvey.do";
        }

        // 전체 통계 데이터 조회
        Map<String, Object> stats = surveyService.getQuestionStats(searchVO.getSurveyId());

        model.addAttribute("survey", surveyForm);
        model.addAttribute("stats", stats);

        return "admin/mgt/survey/surveyStats";
    }

    /**
     * 설문 응답자 목록 조회 (API)
     */
    @GetMapping("/getRespondentList.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<Map<String, Object>> getRespondentList(
            @org.springframework.web.bind.annotation.RequestParam String surveyId) {
        return surveyService.getRespondentList(surveyId);
    }

    /**
     * 특정 응답자의 상세 답변 조회 (API)
     */
    @GetMapping("/getRespondentDetail.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public Map<String, Object> getRespondentDetail(
            @org.springframework.web.bind.annotation.RequestParam String surveyId,
            @org.springframework.web.bind.annotation.RequestParam String resultsId) {
        return surveyService.getRespondentDetail(surveyId, resultsId);
    }

    /**
     * 특정 응답 삭제 (API)
     */
    @org.springframework.web.bind.annotation.PostMapping("/deleteResponse.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public Map<String, Object> deleteResponse(
            @org.springframework.web.bind.annotation.RequestParam String surveyId,
            @org.springframework.web.bind.annotation.RequestParam int resultsId) {
        Map<String, Object> result = new HashMap<>();
        try {
            surveyService.deleteSurveyResponse(surveyId, resultsId);
            result.put("status", "success");
            result.put("message", "삭제되었습니다.");
        } catch (Exception e) {
            log.error("Response Delete Error", e);
            result.put("status", "error");
            result.put("message", "삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    /**
     * 설문 응답 데이터 CSV 다운로드
     */
    @GetMapping("/downloadCsv.do")
    public void downloadCsv(@org.springframework.web.bind.annotation.RequestParam String surveyId,
            jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        String csvData = surveyService.getSurveyDataAsCsv(surveyId);

        String fileName = "survey_results_" + surveyId + ".csv";
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Excel 인식용 UTF-8 BOM 추가
        response.getOutputStream().write(0xEF);
        response.getOutputStream().write(0xBB);
        response.getOutputStream().write(0xBF);

        response.getOutputStream().write(csvData.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }

    /**
     * 설문 응답 건수 조회 (API)
     */
    @GetMapping("/getResponseCount.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public int getResponseCount(@org.springframework.web.bind.annotation.RequestParam String surveyId) {
        return surveyService.getResponseCount(surveyId);
    }

    /**
     * 설문 초기화 및 DRAFT 전환 (API)
     */
    @org.springframework.web.bind.annotation.PostMapping("/resetSurvey.do")
    @org.springframework.web.bind.annotation.ResponseBody
    public Map<String, Object> resetSurvey(@org.springframework.web.bind.annotation.RequestParam String surveyId) {
        Map<String, Object> result = new HashMap<>();
        try {
            surveyService.resetSurveyForEdit(surveyId);
            result.put("status", "success");
            result.put("message", "설문이 초기화되어 수정 가능 상태(DRAFT)로 변경되었습니다.");
        } catch (Exception e) {
            log.error("Survey Reset Error", e);
            result.put("status", "error");
            result.put("message", "초기화 중 오류가 발생했습니다.");
        }
        return result;
    }
}
