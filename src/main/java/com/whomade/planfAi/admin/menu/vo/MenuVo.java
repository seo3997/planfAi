package com.whomade.planfAi.admin.menu.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MenuVo {
    private String menuId; // 메뉴 ID (4자리 단위 확장)
    private String menuTyCode; // 메뉴 유형 코드 (10:카운터리, 20:메뉴, 30:기능)
    private Integer menuLevel; // 메뉴 레벨
    private String menuNm; // 메뉴명
    private String url; // URL
    private Integer sortOrdr; // 정렬 순서
    private String useYn; // 사용 여부

    // 계층형 정렬 필드
    private Integer sortOrdr1;
    private Integer sortOrdr2;
    private Integer sortOrdr3;
    private Integer sortOrdr4;
    private Integer sortOrdr5;
    private Integer sortOrdr6;

    private Integer registerNo; // 등록자 번호
    private LocalDateTime registDt; // 등록 일시
    private Integer updusrNo; // 수정자 번호
    private LocalDateTime updtDt; // 수정 일시

    // UI/편의용 필드
    private String parentMenuId; // 부모 메뉴 ID (등록용)
    private String subMenuId; // 사용자가 입력한 하위 4자리 ID
}
