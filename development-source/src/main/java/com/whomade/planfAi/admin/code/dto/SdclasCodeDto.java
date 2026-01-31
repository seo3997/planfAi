package com.whomade.planfAi.admin.code.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SdclasCodeDto {
    private String groupId;
    private String code;
    private String sclasCode;
    private String sdclasCode;
    private String sdclasNm;
    private String attrb1;
    private String attrb2;
    private String attrb3;
    private Integer sortOrdr;
    private String useYn;
    private Long registerNo;
    private LocalDateTime registDt;
    private Long updusrNo;
    private LocalDateTime updtDt;
}
