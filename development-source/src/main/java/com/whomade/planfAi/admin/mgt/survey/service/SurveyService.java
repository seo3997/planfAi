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
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
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

    /**
     * 설문 응답 저장
     */
    @Transactional
    public void saveSurveyResponse(com.whomade.planfAi.admin.mgt.survey.vo.SurveyResponseDto dto) {
        // 1. 응답자 식별 ID 생성 (UUID의 해시값을 사용하여 정수형으로 변환)
        int resultsId = Math.abs(UUID.randomUUID().hashCode());
        String surveyId = dto.getSurveyId();

        for (com.whomade.planfAi.admin.mgt.survey.vo.SurveyResponseDto.Answer answer : dto.getAnswers()) {
            // A. 기본 응답 저장 (tb_results)
            com.whomade.planfAi.admin.mgt.survey.vo.TbResults res = new com.whomade.planfAi.admin.mgt.survey.vo.TbResults();
            res.setResultsId(resultsId);
            res.setSurveyId(surveyId);
            res.setSectionId(answer.getSectionId());
            res.setQuestionId(answer.getQuestionId());
            res.setOtherAnswerResult(answer.getOtherResult());

            // inputLocation, inputImage, inputTextline 등 처리
            // 프론트엔드에서 이미 "lat,lng" 또는 "path/to/img" 형식으로 result를 보내준다고 가정
            res.setQuestionResult(answer.getResult());

            surveyMapper.insertResults(res);

            // B. 다중 선택 또는 상세 라벨 응답 저장 (tb_results_label)
            if (answer.getLabelIds() != null && !answer.getLabelIds().isEmpty()) {
                for (Integer labelId : answer.getLabelIds()) {
                    com.whomade.planfAi.admin.mgt.survey.vo.TbResultsLabel rl = new com.whomade.planfAi.admin.mgt.survey.vo.TbResultsLabel();
                    rl.setResultsId(resultsId);
                    rl.setResultsLabelId(Math.abs(java.util.UUID.randomUUID().hashCode())); // 고유 ID 추가
                    rl.setSurveyId(surveyId);
                    rl.setSectionId(answer.getSectionId());
                    rl.setQuestionId(answer.getQuestionId());
                    rl.setQuestionLabelId(labelId);
                    rl.setQuestionLabelResult("Y");
                    surveyMapper.insertResultsLabel(rl);
                }
            }
        }
    }

    /**
     * 설문 전체 통계 데이터 조회
     */
    public Map<String, Object> getQuestionStats(String surveyId) {
        Map<String, Object> allStats = new HashMap<>();

        // 1. 설문 구조 가져오기
        TbSurvey searchVO = new TbSurvey();
        searchVO.setSurveyId(surveyId);
        TbSurvey surveyForm = selectSurveyEntryForm(searchVO);

        if (surveyForm != null && surveyForm.getSections() != null) {
            for (com.whomade.planfAi.admin.mgt.survey.vo.TbSection section : surveyForm.getSections()) {
                if (section.getQuestions() != null) {
                    for (TbQuestion question : section.getQuestions()) {
                        String type = question.getQuestionType();
                        int qId = question.getQuestionId();

                        // 타입별 데이터 수집
                        if ("inputRadio".equals(type) || "inputCheckbox".equals(type)) {
                            // 라벨별 카운트 (Map<LabelId, Count>)
                            List<Map<String, Object>> labelStats = surveyMapper.selectLabelStats(surveyId, qId);
                            Map<String, Integer> countMap = new HashMap<>();
                            for (Map<String, Object> row : labelStats) {
                                Object labelIdObj = row.get("labelId") != null ? row.get("labelId")
                                        : row.get("LABELID"); // 대소문자 대응
                                Object countObj = row.get("count") != null ? row.get("count") : row.get("COUNT");

                                String labelIdStr = labelIdObj != null ? String.valueOf(labelIdObj) : "";
                                int count = 0;
                                if (countObj != null) {
                                    if (countObj instanceof Number) {
                                        count = ((Number) countObj).intValue();
                                    } else {
                                        try {
                                            count = Integer.parseInt(String.valueOf(countObj));
                                        } catch (NumberFormatException e) {
                                            count = 0;
                                        }
                                    }
                                }

                                if (!labelIdStr.isEmpty()) {
                                    countMap.put(labelIdStr, count);
                                }
                            }

                            // '기타' 응답 카운트 추가
                            if ("Y".equals(question.getOtherAnswer())) {
                                int otherCount = surveyMapper.countOtherResults(surveyId, qId);
                                countMap.put("OTHER", otherCount);

                                // 기타 텍스트 응답 목록도 별도 키로 저장 (필요시)
                                List<com.whomade.planfAi.admin.mgt.survey.vo.TbResults> otherTexts = surveyMapper
                                        .selectOtherTextResults(surveyId, qId);
                                allStats.put(qId + "_other_texts", otherTexts);
                            }

                            allStats.put(String.valueOf(qId), countMap);
                        } else if ("inputLocation".equals(type)) {
                            // 위경도 리스트
                            List<Map<String, Object>> locResults = surveyMapper.selectLocationResults(surveyId, qId);
                            List<Map<String, Object>> formattedLocs = new ArrayList<>();
                            for (Map<String, Object> row : locResults) {
                                Map<String, Object> loc = new HashMap<>();
                                try {
                                    loc.put("lat", Double.parseDouble(String.valueOf(row.get("lat"))));
                                    loc.put("lng", Double.parseDouble(String.valueOf(row.get("lng"))));
                                    formattedLocs.add(loc);
                                } catch (Exception e) {
                                }
                            }
                            allStats.put(String.valueOf(qId), formattedLocs);
                        } else {
                            // 텍스트/이미지 리스트
                            allStats.put(String.valueOf(qId), surveyMapper.selectTextResults(surveyId, qId));
                        }
                    }
                }
            }
        }
        return allStats;
    }

    /**
     * 질문별 통계 데이터 조회 (개별 호출용)
     */
    public Map<String, Object> getQuestionStats(String surveyId, int questionId, String type) {
        Map<String, Object> stats = new HashMap<>();

        if ("inputRadio".equals(type) || "inputCheckbox".equals(type)) {
            stats.put("labels", surveyMapper.selectLabelStats(surveyId, questionId));
        } else if ("inputLocation".equals(type)) {
            stats.put("locations", surveyMapper.selectLocationResults(surveyId, questionId));
        } else {
            stats.put("results", surveyMapper.selectTextResults(surveyId, questionId));
        }

        return stats;
    }

    /**
     * 설문 응답자 목록 조회
     */
    public List<Map<String, Object>> getRespondentList(String surveyId) {
        return surveyMapper.selectRespondentList(surveyId);
    }

    /**
     * 특정 응답자의 상세 답변 조회
     */
    public Map<String, Object> getRespondentDetail(String surveyId, String resultsId) {
        Map<String, Object> detail = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("surveyId", surveyId);
        params.put("resultsId", resultsId);

        // 1. 일반 응답 (주관식, 위치, 이미지 등)
        List<com.whomade.planfAi.admin.mgt.survey.vo.TbResults> resList = surveyMapper.selectRespondentDetail(params);
        for (com.whomade.planfAi.admin.mgt.survey.vo.TbResults res : resList) {
            String qId = String.valueOf(res.getQuestionId());
            detail.put(qId, res.getQuestionResult());

            // '기타' 입력값 처리
            if ("OTHER".equals(res.getQuestionResult())) {
                detail.put(qId + "_other", res.getOtherAnswerResult());
            }
        }

        // 2. 객관식 응답 (라벨 아이디들)
        List<com.whomade.planfAi.admin.mgt.survey.vo.TbResultsLabel> labelList = surveyMapper
                .selectRespondentLabelDetail(params);
        for (com.whomade.planfAi.admin.mgt.survey.vo.TbResultsLabel rl : labelList) {
            String qId = String.valueOf(rl.getQuestionId());
            Object existing = detail.get(qId);
            List<Object> list;
            if (existing instanceof List) {
                list = (List<Object>) existing;
            } else {
                list = new ArrayList<>();
                if (existing != null)
                    list.add(existing);
                detail.put(qId, list);
            }
            list.add(rl.getQuestionLabelId());
        }

        return detail;
    }

    /**
     * 특정 응답 삭제 (응답 + 라벨)
     */
    @Transactional
    public void deleteSurveyResponse(String surveyId, int resultsId) {
        surveyMapper.deleteResultsLabelByResultsId(surveyId, resultsId);
        surveyMapper.deleteResultsByResultsId(surveyId, resultsId);
    }

    /**
     * 설문 응답 데이터 CSV 생성
     */
    public String getSurveyDataAsCsv(String surveyId) {
        // 1. 설문 구조 조회 (질문 목록 추출을 위함)
        TbSurvey searchVO = new TbSurvey();
        searchVO.setSurveyId(surveyId);
        TbSurvey survey = selectSurveyEntryForm(searchVO);
        if (survey == null)
            return "";

        List<TbQuestion> allQuestions = new ArrayList<>();
        if (survey.getSections() != null) {
            for (TbSection s : survey.getSections()) {
                if (s.getQuestions() != null) {
                    allQuestions.addAll(s.getQuestions());
                }
            }
        }

        // 2. CSV 헤더 생성 (첫 행: 질문 내용)
        StringBuilder sb = new StringBuilder();
        sb.append("응답시간,응답ID");
        for (TbQuestion q : allQuestions) {
            String title = (q.getQuestionText() != null ? q.getQuestionText() : "질문 " + q.getQuestionId())
                    .replace("\"", "\"\"");
            sb.append(",\"").append(title).append("\"");
        }
        sb.append("\n");

        // 3. 응답자 목록 조회
        List<Map<String, Object>> respondents = getRespondentList(surveyId);

        // 4. 응답 데이터 채우기
        for (Map<String, Object> r : respondents) {
            String rId = String.valueOf(r.get("resultsId"));
            String registDt = String.valueOf(r.get("registDt"));

            sb.append(registDt).append(",").append(rId);

            // 해당 응답자의 상세 답변 데이터 조회
            Map<String, Object> detail = getRespondentDetail(surveyId, rId);

            for (TbQuestion q : allQuestions) {
                sb.append(",");
                String qId = String.valueOf(q.getQuestionId());
                Object ans = detail.get(qId);

                String cellValue = "";
                if (ans != null) {
                    if (ans instanceof List) {
                        // 다중 선택 (Checkbox)
                        List<?> labelIds = (List<?>) ans;
                        List<String> labels = new ArrayList<>();
                        for (Object lid : labelIds) {
                            String labelText = findLabelText(q, lid);
                            if (labelText != null)
                                labels.add(labelText);
                        }

                        // '기타' 처리 (결과 본문에 'OTHER'가 있을 경우)
                        String rawRes = getRawResult(surveyId, rId, q.getQuestionId());
                        if ("OTHER".equals(rawRes)) {
                            String other = (String) detail.get(qId + "_other");
                            labels.add("기타: " + (other != null ? other : ""));
                        }

                        cellValue = String.join("; ", labels);
                    } else {
                        // 단일 선택 (Radio) 또는 기타 주관식
                        if ("OTHER".equals(ans)) {
                            String other = (String) detail.get(qId + "_other");
                            cellValue = "기타: " + (other != null ? other : "");
                        } else if ("inputRadio".equals(q.getQuestionType())) {
                            cellValue = findLabelText(q, ans);
                        } else {
                            cellValue = String.valueOf(ans);
                        }
                    }
                }

                // CSV 특수문자 이스케이프
                cellValue = cellValue.replace("\"", "\"\"");
                sb.append("\"").append(cellValue).append("\"");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private String getRawResult(String surveyId, String rId, Integer qId) {
        Map<String, Object> params = new HashMap<>();
        params.put("surveyId", surveyId);
        params.put("resultsId", rId);
        List<com.whomade.planfAi.admin.mgt.survey.vo.TbResults> resList = surveyMapper.selectRespondentDetail(params);
        for (com.whomade.planfAi.admin.mgt.survey.vo.TbResults r : resList) {
            if (r.getQuestionId().equals(qId)) {
                return r.getQuestionResult();
            }
        }
        return null;
    }

    private String findLabelText(TbQuestion q, Object lid) {
        if (q.getLabels() == null)
            return String.valueOf(lid);
        for (TbQuestionLabel l : q.getLabels()) {
            if (String.valueOf(l.getQuestionLabelId()).equals(String.valueOf(lid))) {
                return l.getQuestionLabel();
            }
        }
        return String.valueOf(lid);
    }
}
