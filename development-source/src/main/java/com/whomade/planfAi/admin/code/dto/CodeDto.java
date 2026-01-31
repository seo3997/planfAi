package com.whomade.planfAi.admin.code.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CodeDto {
    private String groupId;
    private String code;
    private String codeNm;
    private String codeNmEng;
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
