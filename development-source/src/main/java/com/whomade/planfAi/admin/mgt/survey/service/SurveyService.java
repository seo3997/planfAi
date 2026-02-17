package com.whomade.planfAi.admin.mgt.survey.service;

import com.whomade.planfAi.admin.mgt.survey.mapper.SurveyMapper;
import com.whomade.planfAi.admin.mgt.survey.vo.TbQuestion;
import com.whomade.planfAi.admin.mgt.survey.vo.TbQuestionLabel;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSection;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSurvey;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSurveyTheme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyMapper surveyMapper;

    /**
     * 테마 목록 조회
     */
    public List<TbSurveyTheme> selectThemeList() {
        return surveyMapper.selectThemeList();
    }

    /**
     * 테마 상세 조회
     */
    public TbSurveyTheme selectThemeDetail(int themeSeq) {
        return surveyMapper.selectThemeDetail(themeSeq);
    }

    /**
     * 설문 목록 조회
     */
    public List<TbSurvey> selectSurveyList(TbSurvey vo) {
        return surveyMapper.selectSurveyList(vo);
    }

    /**
     * 설문 목록 총 건수 조회
     */
    public int selectSurveyCount(TbSurvey vo) {
        return surveyMapper.selectSurveyCount(vo);
    }

    /**
     * 설문 상세 조회
     */
    public TbSurvey selectSurveyDetail(TbSurvey vo) {
        return surveyMapper.selectSurveyDetail(vo);
    }

    /**
     * 설문 등록
     */
    @Transactional
    public int insertSurvey(TbSurvey vo) {
        if (vo.getSurveyId() == null || vo.getSurveyId().isEmpty()) {
            // ID 자동 생성 (예: SVY_UUID)
            vo.setSurveyId("SVY_" + UUID.randomUUID().toString().substring(0, 8));
        }
        return surveyMapper.insertSurvey(vo);
    }

    /**
     * 설문 수정
     */
    @Transactional
    public int updateSurvey(TbSurvey vo) {
        return surveyMapper.updateSurvey(vo);
    }

    /**
     * 설문 전체 구조 조회 (Survey + Sections + Questions + Labels)
     */
    public TbSurvey selectSurveyEntryForm(TbSurvey vo) {
        // 1. 설문 기본 정보 조회
        TbSurvey survey = surveyMapper.selectSurveyDetail(vo);
        if (survey == null)
            return null;

        // 2. 전체 섹션 조회
        List<TbSection> sections = surveyMapper.selectSectionList(vo);

        // 3. 전체 질문 조회
        List<TbQuestion> questions = surveyMapper.selectQuestionList(vo);

        // 4. 전체 보기(Label) 조회 (객관식 문항용)
        List<TbQuestionLabel> labels = surveyMapper.selectQuestionLabelList(vo);

        // 5. 계층 구조 조립 (Labels -> Questions -> Sections -> Survey)
        // 5-1. Labels -> Questions Mapping
        if (questions != null && labels != null) {
            for (TbQuestion q : questions) {
                List<TbQuestionLabel> qLabels = labels.stream()
                        .filter(l -> l.getQuestionId().equals(q.getQuestionId())
                                && l.getSectionId().equals(q.getSectionId()))
                        .toList();
                q.setLabels(qLabels);
            }
        }

        // 5-2. Questions -> Sections Mapping
        if (sections != null && questions != null) {
            for (TbSection s : sections) {
                List<TbQuestion> sQuestions = questions.stream()
                        .filter(q -> q.getSectionId().equals(s.getSectionId()))
                        .toList();
                s.setQuestions(sQuestions);
            }
        }

        // 5-3. Sections -> Survey Mapping
        survey.setSections(sections);

        return survey;
    }

    /**
     * 설문 전체 구조 저장 (Survey + Sections + Questions + Labels)
     */
    @Transactional
    public void saveSurveyEntryForm(TbSurvey vo) throws Exception {
        // 1. 설문 기본 정보 저장/수정
        if (vo.getSurveySeq() == null || vo.getSurveySeq() == 0) {
            String newId = "SVY_" + UUID.randomUUID().toString().substring(0, 8);
            vo.setSurveyId(newId);
            surveyMapper.insertSurvey(vo);
        } else {
            surveyMapper.updateSurvey(vo);
        }

        String surveyId = vo.getSurveyId();

        // 2. 기존 하위 데이터 삭제 (Full Refresh 전략)
        surveyMapper.deleteQuestionLabelBySurveyId(surveyId);
        surveyMapper.deleteQuestionBySurveyId(surveyId);
        surveyMapper.deleteSectionBySurveyId(surveyId);

        // 3. 신규 하위 데이터 등록
        List<TbSection> sections = vo.getSections();
        if (sections != null) {
            for (TbSection section : sections) {
                section.setSurveyId(surveyId);
                section.setRegisterNo(vo.getRegisterNo());
                section.setUpdusrNo(vo.getUpdusrNo());
                surveyMapper.insertSection(section);

                List<TbQuestion> questions = section.getQuestions();
                if (questions != null) {
                    for (TbQuestion question : questions) {
                        question.setSurveyId(surveyId);
                        question.setSectionId(section.getSectionId()); // Ensure Section ID consistency
                        question.setRegisterNo(vo.getRegisterNo());
                        question.setUpdusrNo(vo.getUpdusrNo());
                        surveyMapper.insertQuestion(question);

                        List<TbQuestionLabel> labels = question.getLabels();
                        if (labels != null) {
                            for (TbQuestionLabel label : labels) {
                                label.setSurveyId(surveyId);
                                label.setSectionId(section.getSectionId());
                                label.setQuestionId(question.getQuestionId());
                                label.setRegisterNo(vo.getRegisterNo());
                                label.setUpdusrNo(vo.getUpdusrNo());
                                surveyMapper.insertQuestionLabel(label);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 설문 삭제
     */
    @Transactional
    public int deleteSurvey(TbSurvey vo) {
        // 하위 데이터 삭제 (Cascade 설정이 없으므로 수동 삭제)
        surveyMapper.deleteQuestionLabelBySurveyId(vo.getSurveyId());
        surveyMapper.deleteQuestionBySurveyId(vo.getSurveyId());
        surveyMapper.deleteSectionBySurveyId(vo.getSurveyId());
        return surveyMapper.deleteSurvey(vo);
    }

    /**
     * 설문 복사
     */
    @Transactional
    public String copySurvey(String originalSurveyId) throws Exception {
        TbSurvey searchVO = new TbSurvey();
        searchVO.setSurveyId(originalSurveyId);

        // 1. 원본 데이터 조회
        TbSurvey original = selectSurveyEntryForm(searchVO);
        if (original == null)
            throw new Exception("원본 설문을 찾을 수 없습니다.");

        // 2. 새 복사본 생성
        TbSurvey copy = new TbSurvey();
        // 기본 정보 복사 (주요 필드 위주)
        copy.setSurveyTitle("[복사본] " + original.getSurveyTitle());
        copy.setOpened("DRAFT"); // 복사본은 기본적으로 작성중 상태
        copy.setAdminEmail(original.getAdminEmail());
        copy.setBgColor(original.getBgColor());
        copy.setTextColor(original.getTextColor());
        copy.setAccentColor(original.getAccentColor());
        copy.setFontSize(original.getFontSize());
        copy.setFontFamily(original.getFontFamily());
        copy.setHeader(original.getHeader());
        copy.setFooter(original.getFooter());
        copy.setThemeSeq(original.getThemeSeq());
        copy.setLayoutType(original.getLayoutType());
        copy.setLogoImagePath(original.getLogoImagePath());
        copy.setLogoAlign(original.getLogoAlign());
        copy.setShowFooterLogo(original.getShowFooterLogo());
        copy.setShowBorder(original.getShowBorder());
        copy.setUserCss(original.getUserCss());

        // 3. 전체 구조 저장 로직 활용
        // saveSurveyEntryForm expects surveySeq == null for new insertion
        copy.setSections(original.getSections());
        saveSurveyEntryForm(copy);

        return copy.getSurveyId();
    }
}
