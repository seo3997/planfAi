package com.whomade.planfAi.admin.user.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long userNo;
    private String userId;
    private String password;
    private String userNm;
    private String cttpcSeCode;
    private String cttpc;
    private String email;
    private String areaCode;
    private String areaSeCodeS;
    private String areaSeCodeD;
    private String userSttusCode;
    private String userSttusNm;
    private LocalDateTime loginDt;
    private String userAge;
    private String birthDate;
    private Integer gender;
    private String authorId;
    private String authorNm; // Join with op_author
    private Integer citizenshipType;
    private String referrerId;
    private String deviceType;
    private String pushToken;
    private Integer registerNo;
    private String registerNm;
    private LocalDateTime registDt;
    private Integer updusrNo;
    private LocalDateTime updtDt;

    // Search fields
    private String searchStatus;
    private String searchAuthor;
    private String searchType;
    private String searchKeyword;
    private String startDate;
    private String endDate;
}
