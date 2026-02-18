package com.whomade.planfAi.admin.mgt.survey.mapper;

import com.whomade.planfAi.admin.mgt.survey.vo.TbQuestion;
import com.whomade.planfAi.admin.mgt.survey.vo.TbQuestionLabel;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSection;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSurvey;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSurveyTheme;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyMapper {

        /**
         * 테마 목록 조회
         */
        List<TbSurveyTheme> selectThemeList();

        /**
         * 테마 상세 조회
         */
        TbSurveyTheme selectThemeDetail(int themeSeq);

        /**
         * 설문 목록 조회 (페이징)
         * 
         * @param vo 검색 조건 및 페이징 정보
         * @return 설문 목록
         */
        List<TbSurvey> selectSurveyList(TbSurvey vo);

        /**
         * 설문 목록 총 건수 조회
         * 
         * @param vo 검색 조건
         * @return 총 건수
         */
        int selectSurveyCount(TbSurvey vo);

        /**
         * 설문 상세 조회
         * 
         * @param vo (surveySeq 또는 surveyId)
         * @return 설문 상세 정보
         */
        TbSurvey selectSurveyDetail(TbSurvey vo);

        /**
         * 설문 등록
         * 
         * @param vo 설문 정보
         * @return 등록 건수
         */
        int insertSurvey(TbSurvey vo);

        /**
         * 설문 수정
         * 
         * @param vo 설문 정보
         * @return 수정 건수
         */
        int updateSurvey(TbSurvey vo);

        /**
         * 설문 삭제
         * 
         * @param vo 설문 정보
         * @return 삭제 건수
         */
        int deleteSurvey(TbSurvey vo);

        // --- Section ---
        List<TbSection> selectSectionList(TbSurvey vo);

        int insertSection(TbSection vo);

        int deleteSectionBySurveyId(String surveyId);

        // --- Question ---
        List<TbQuestion> selectQuestionList(TbSurvey vo);

        int insertQuestion(TbQuestion vo);

        int deleteQuestionBySurveyId(String surveyId);

        // --- Question Label ---
        List<TbQuestionLabel> selectQuestionLabelList(TbSurvey vo);

        int insertQuestionLabel(TbQuestionLabel vo);

        int deleteQuestionLabelBySurveyId(String surveyId);

        // --- Survey Results & Stats ---
        int insertResults(com.whomade.planfAi.admin.mgt.survey.vo.TbResults vo);

        int insertResultsLabel(com.whomade.planfAi.admin.mgt.survey.vo.TbResultsLabel vo);

        /**
         * 특정 질문의 라벨별 응답 통계 (객관식)
         */
        List<java.util.Map<String, Object>> selectLabelStats(String surveyId, int questionId);

        /**
         * 특정 질문의 '기타' 응답 수 (객관식)
         */
        int countOtherResults(String surveyId, int questionId);

        /**
         * 특정 질문의 '기타' 응답 텍스트 리스트
         */
        List<com.whomade.planfAi.admin.mgt.survey.vo.TbResults> selectOtherTextResults(String surveyId, int questionId);

        /**
         * 특정 질문의 주관식 응답 목록
         */
        List<com.whomade.planfAi.admin.mgt.survey.vo.TbResults> selectTextResults(String surveyId, int questionId);

        /**
         * 위치 정보 응답 목록 (지도 마커용)
         */
        List<java.util.Map<String, Object>> selectLocationResults(String surveyId, int questionId);

        /**
         * 설문 응답자 목록 조회
         */
        List<java.util.Map<String, Object>> selectRespondentList(String surveyId);

        /**
         * 특정 응답자의 전체 답변 조회
         */
        List<com.whomade.planfAi.admin.mgt.survey.vo.TbResults> selectRespondentDetail(
                        java.util.Map<String, Object> params);

        /**
         * 특정 응답자의 객관식 선택 결과 조회
         */
        List<com.whomade.planfAi.admin.mgt.survey.vo.TbResultsLabel> selectRespondentLabelDetail(
                        java.util.Map<String, Object> params);
}
