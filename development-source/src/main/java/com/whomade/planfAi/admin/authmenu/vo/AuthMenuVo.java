package com.whomade.planfAi.admin.authmenu.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuthMenuVo {
    private String menuId;
    private String menuTyCode;
    private Integer menuLevel;
    private String menuNm;
    private String url;
    private Integer sortOrdr;
    private String useYn;
    private String authYn; // Y if authority is granted, N otherwise

    // Sorting fields for tree structure
    private Integer sortOrdr1;
    private Integer sortOrdr2;
    private Integer sortOrdr3;
    private Integer sortOrdr4;
    private Integer sortOrdr5;
    private Integer sortOrdr6;

    private String authorId;
    private Integer registerNo;
    private LocalDateTime registDt;
}
