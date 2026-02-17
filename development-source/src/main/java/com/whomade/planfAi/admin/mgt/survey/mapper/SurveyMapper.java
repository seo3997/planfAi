package com.whomade.planfAi.admin.mgt.survey.mapper;

import com.whomade.planfAi.admin.mgt.survey.vo.TbQuestion;
import com.whomade.planfAi.admin.mgt.survey.vo.TbQuestionLabel;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSection;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSurvey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyMapper {

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
}
