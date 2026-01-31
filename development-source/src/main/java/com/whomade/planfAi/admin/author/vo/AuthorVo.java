package com.whomade.planfAi.admin.author.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuthorVo {
    private String authorId;
    private String authorNm;
    private String rm;
    private Integer registerNo;
    private String registerNm;
    private LocalDateTime registDt;
    private String registYmd;
    private Integer updusrNo;
    private LocalDateTime updtDt;
    private String updtYmd;

    // Search fields
    private String schAuthNm;
}
