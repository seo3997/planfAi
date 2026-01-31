package com.whomade.planfAi.admin.mgt.board.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BoardVo {
    private Long bbsSeq;
    private String bbsSeCodeL;
    private String bbsSeCodeM;
    private String sj;
    private String cn;
    private String atchDocId;
    private Long hitCnt;
    private String atchYn;
    private String deleteYn;
    private Integer registerNo;
    private LocalDateTime registDt;
    private Integer updusrNo;
    private LocalDateTime updtDt;

    // Joint fields
    private String registerNm;
    private String bbsSeNm;
}
