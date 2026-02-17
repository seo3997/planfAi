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

@Controller
@RequestMapping("/mgt/survey")
@RequiredArgsConstructor
@Slf4j
public class SurveyController {

    private final SurveyService surveyService;

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
}
