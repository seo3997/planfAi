package com.whomade.planfAi.admin.mgt.survey.vo;

import lombok.Data;
import java.util.Date;

@Data
public class TbSurveyTheme {
    private Integer themeSeq;
    private String themeName;
    private String themeImagePath;
    private String bgImagePath;
    private String bgColor;
    private String textColor;
    private String accentColor;
    private String layoutType;
    private String isAccessibility;
    private Date registDt;
}
