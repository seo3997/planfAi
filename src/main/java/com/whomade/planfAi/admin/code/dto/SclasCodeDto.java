package com.whomade.planfAi.admin.code.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SclasCodeDto {
    private String groupId;
    private String code;
    private String sclasCode;
    private String sclasNm;
    private String attrb1;
    private String attrb2;
    private String attrb3;
    private String attrb4;
    private Integer sortOrdr;
    private String useYn;
    private Long registerNo;
    private LocalDateTime registDt;
    private Long updusrNo;
    private LocalDateTime updtDt;
}
