package com.whomade.planfAi.admin.mgt.survey.vo;

import lombok.Data;
import java.util.Date;

@Data
public class TbResults {
    private Integer resultsSeq;
    private Integer resultsId;
    private String surveyId;
    private Integer sectionId;
    private Integer questionId;
    private String questionResult; // Can store text, image path, or lat,lng
    private String otherAnswerResult;
    private Integer registerNo;
    private String registDt;
    private Integer updusrNo;
    private String updtDt;
}
