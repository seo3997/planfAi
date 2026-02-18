package com.whomade.planfAi.admin.mgt.survey.vo;

import lombok.Data;
import java.util.List;

@Data
public class SurveyResponseDto {
    private String surveyId;
    private List<Answer> answers;

    @Data
    public static class Answer {
        private Integer sectionId;
        private Integer questionId;
        private String questionType;
        private String result; // For text, radio, or formatted location (lat,lng)
        private List<Integer> labelIds; // For checkboxes (multiple selected labels)
        private String otherResult; // For "Other" input
    }
}
