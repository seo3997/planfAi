package com.whomade.planfAi.admin.mgt.survey.vo;

import lombok.Data;
import java.util.Date;

@Data
public class TbQuestionLabel {
    // PK: QUESTION_LABEL_SEQ
    private Integer questionLabelSeq;

    // FK
    private String surveyId;
    private Integer sectionId;
    private Integer questionId;

    // 비즈니스 키
    private Integer questionLabelId;

    // 속성
    private String questionLabel; // 보기 텍스트
    private String exportCode;
    private String selected;

    // 공통 이력 컬럼
    private Integer registerNo;
    private Date registDt;
    private Integer updusrNo;
    private Date updtDt;
}
