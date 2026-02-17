package com.whomade.planfAi.admin.mgt.survey.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class TbQuestion {
    // PK: QUESTION_SEQ
    private Integer questionSeq;

    // FK
    private String surveyId;
    private Integer sectionId;

    // 비즈니스 키
    private Integer questionId;

    // 속성
    private String questionType; // inputRadio, inputCheckbox, inputTextline, etc.
    private String showDivider;
    private String exportInclude;
    private String exportExpand;
    private String questionText;
    private String questionCd;
    private String questionIsHtml;
    private String questionLayout;
    private String questionLabel; // 질문 제목? (schema: QUESTION_LABEL) -> Label Table? NO, check schema.
    // In schema.sql, tb_question has QUESTION_LABEL column too. It might be the
    // question text displayed.

    private String otherAnswer;
    private Integer textCols;
    private Integer textRows;
    private Integer textSize;
    private Integer maxLength;
    private String summaryCd;

    // 공통 이력 컬럼
    private Integer registerNo;
    private Date registDt;
    private Integer updusrNo;
    private Date updtDt;

    // 계층 구조 (1:N) - 객관식 보기
    private List<TbQuestionLabel> labels;
}
