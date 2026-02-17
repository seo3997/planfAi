package com.whomade.planfAi.admin.mgt.survey.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class TbSurvey {
    // PK
    private Integer surveySeq;

    // 비즈니스 키
    private String surveyId;
    private String surveyTitle;

    // 상태 및 권한
    private String opened; // OPENED, CLOSED
    private String closed;
    private String adminEmail;
    private String adminPid;
    private String accessResultsRestriction;
    private String resultsPassword;
    private String entryRestriction;
    private String entryPassword;

    // 설정
    private String members;
    private String oneEntryOnly;
    private String showBorder;
    private String exportDelimiter;
    private String exportIncludeQuestions;
    private String ip;
    private String browserId;

    // 테마 (Theme)
    private String bgColor;
    private String textColor;
    private String fontSize;
    private String header;
    private String footer;
    private String baseHref;
    private String userCss;
    private String divider;

    // 테마 및 디자인 고도화 필드
    private Integer themeSeq;
    private String bgImagePath;
    private String logoImagePath;
    private String logoAlign;
    private String showFooterLogo;
    private String layoutType;
    private String fontFamily;
    private String accentColor;

    // 문구
    private String exitPageTextIsHtml;
    private String exitPageText;
    private String numberRoductionText;

    // 공통 이력 컬럼
    private Integer registerNo;
    private String registDt;
    private Integer updusrNo;
    private String updtDt;

    // 페이징 (검색조건)
    private int pageIndex = 1;
    private int pageSize = 10;
    private int firstIndex = 1;
    private int lastIndex = 1;
    private int recordCountPerPage = 10;

    private String searchCondition;
    private String searchKeyword;

    // 계층 구조 (1:N)
    private List<TbSection> sections;
}
