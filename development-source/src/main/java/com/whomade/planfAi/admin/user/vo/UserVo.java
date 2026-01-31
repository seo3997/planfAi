package com.whomade.planfAi.admin.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVo {
    private Long userNo;
    private String userId;
    private String password;
    private String userNm;
    private String cttpcSeCode;
    private String cttpc;
    private String email;
    private String areaCode; // 대분류
    private String areaSeCodeS; // 중분류
    private String areaSeCodeD; // 소분류
    private String userSttusCode;
    private LocalDateTime loginDt;
    private String userAge;
    private String birthDate;
    private Boolean gender;
    private String authorId;
    private Boolean citizenshipType;
    private String referrerId;
    private String deviceType;
    private String pushToken;
    private Integer registerNo;
    private LocalDateTime registDt;
    private Integer updusrNo;
    private LocalDateTime updtDt;

    // 조회용 추가 필드
    private String authorNm;
    private String userSttusNm;
    private String areaNm;
}
