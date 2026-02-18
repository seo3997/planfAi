package com.whomade.planfAi.admin.mgt.survey.vo;

import lombok.Data;

@Data
public class TbResultsLabel {
    private Integer resultsLabelSeq;
    private Integer resultsId;
    private Integer resultsLabelId;
    private String surveyId;
    private Integer sectionId;
    private Integer questionId;
    private Integer questionLabelId;
    private String questionLabelResult;
    private Integer registerNo;
    private String registDt;
    private Integer updusrNo;
    private String updtDt;
}
