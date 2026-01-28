package com.whomade.planfAi.admin.code.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CodeGroupDto {
    private String groupId;
    private String groupNm;
    private String groupNmEng;
    private String rm;
    private Long registerNo;
    private LocalDateTime registDt;
    private Long updusrNo;
    private LocalDateTime updtDt;

    // UI mapping fields
    private String registerNm;
    private String registYmd;
}
