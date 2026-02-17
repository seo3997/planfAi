package com.whomade.planfAi.admin.mgt.survey.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class TbSection {
    // PK: SURVEY_ID, SECTION_ID
    private Integer sectionSeq; // Unique Key
    private String surveyId;
    private Integer sectionId;

    private String sectionTitle;
    private String sectionHtmlTitle;

    // 공통 이력 컬럼
    private Integer registerNo;
    private Date registDt;
    private Integer updusrNo;
    private Date updtDt;

    // 계층 구조 (1:N)
    private List<TbQuestion> questions;
}
