-- 사용자 정보
CREATE TABLE `op_user` (
  `USER_NO` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '사용자 고유 번호',
  `USER_ID` varchar(200) NOT NULL COMMENT '사용자 아이디',
  `PASSWORD` varchar(200) NOT NULL COMMENT '비밀번호',
  `USER_NM` varchar(50) NOT NULL COMMENT '사용자 이름',
  `CTTPC_SE_CODE` varchar(4) DEFAULT NULL COMMENT '통신사 구분 코드',
  `CTTPC` varchar(200) DEFAULT NULL COMMENT '통신사 정보',
  `EMAIL` varchar(50) DEFAULT NULL COMMENT '이메일',
  `AREA_CODE` varchar(10) DEFAULT NULL COMMENT '지역 코드',
  `USER_STTUS_CODE` varchar(100) DEFAULT NULL COMMENT '사용자 상태 코드',
  `LOGIN_DT` datetime DEFAULT NULL COMMENT '마지막 로그인 일시',
  `USER_AGE` varchar(255) DEFAULT NULL,
  `BIRTH_DATE` varchar(50) DEFAULT '',
  `GENDER` tinyint(1) DEFAULT NULL,
  `AUTHOR_ID` varchar(20) DEFAULT NULL,
  `CITIZENSHIP_TYPE` tinyint(1) DEFAULT NULL,
  `REFERRER_ID` varchar(50) DEFAULT NULL,
  `DEVICE_TYPE` varchar(20) DEFAULT NULL,
  `PUSH_TOKEN` varchar(512) DEFAULT NULL,
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  `UPDUSR_NO` int DEFAULT NULL,
  `UPDT_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`USER_NO`),
  UNIQUE KEY `uk_email` (`EMAIL`),
  KEY `idx_op_user_user_id` (`USER_ID`)
);

-- 권한 마스터
CREATE TABLE `op_author` (
  `AUTHOR_ID` varchar(20) NOT NULL,
  `AUTHOR_NM` varchar(200) DEFAULT NULL,
  `RM` varchar(200) DEFAULT NULL COMMENT '비고',
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  `UPDUSR_NO` int DEFAULT NULL,
  `UPDT_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`AUTHOR_ID`)
);

-- 메뉴 마스터 (4자리 확장 구조)
CREATE TABLE `op_menu` (
  `MENU_ID` varchar(24) NOT NULL,
  `MENU_TY_CODE` varchar(4) NOT NULL COMMENT '10:카테고리, 20:메뉴, 30:기능',
  `MENU_LEVEL` int DEFAULT NULL,
  `MENU_NM` varchar(200) DEFAULT NULL,
  `URL` varchar(200) DEFAULT NULL,
  `SORT_ORDR` int DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT NULL,
  `SORT_ORDR_1` int DEFAULT NULL,
  `SORT_ORDR_2` int DEFAULT NULL,
  `SORT_ORDR_3` int DEFAULT NULL,
  `SORT_ORDR_4` int DEFAULT NULL,
  `SORT_ORDR_5` int DEFAULT NULL,
  `SORT_ORDR_6` int DEFAULT NULL,
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  `UPDUSR_NO` int DEFAULT NULL,
  `UPDT_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`)
);

-- 권한별 메뉴 매핑
CREATE TABLE `op_author_menu` (
  `MENU_ID` varchar(24) NOT NULL,
  `AUTHOR_ID` varchar(20) NOT NULL,
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`,`AUTHOR_ID`)
);

-- 1단계: 코드 대분류 (Group)
CREATE TABLE `op_code_group` (
  `GROUP_ID` varchar(20) NOT NULL,
  `GROUP_NM` varchar(100) NOT NULL,
  `GROUP_NM_ENG` varchar(100) DEFAULT NULL,
  `RM` varchar(200) DEFAULT NULL,
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  `UPDUSR_NO` int DEFAULT NULL,
  `UPDT_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`)
);

-- 2단계: 코드 중분류 (Code)
CREATE TABLE `op_code` (
  `GROUP_ID` varchar(20) NOT NULL,
  `CODE` varchar(4) NOT NULL,
  `CODE_NM` varchar(100) DEFAULT NULL,
  `CODE_NM_ENG` varchar(100) DEFAULT NULL,
  `ATTRB_1` varchar(100) DEFAULT NULL,
  `ATTRB_2` varchar(100) DEFAULT NULL,
  `ATTRB_3` varchar(100) DEFAULT NULL,
  `SORT_ORDR` int DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT NULL,
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  `UPDUSR_NO` int DEFAULT NULL,
  `UPDT_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`,`CODE`)
);

-- 3단계: 코드 소분류 (Sclas)
CREATE TABLE `op_sclas_code` (
  `GROUP_ID` varchar(20) NOT NULL,
  `CODE` varchar(20) NOT NULL,
  `SCLAS_CODE` varchar(20) NOT NULL,
  `SCLAS_NM` varchar(200) DEFAULT NULL,
  `ATTRB_1` text,
  `ATTRB_2` text,
  `ATTRB_3` text,
  `ATTRB_4` text,
  `SORT_ORDR` int DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT NULL,
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  `UPDUSR_NO` int DEFAULT NULL,
  `UPDT_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`,`CODE`,`SCLAS_CODE`)
);

-- 4단계: 코드 세분류 (Sdclas)
CREATE TABLE `op_sdclas_code` (
  `GROUP_ID` varchar(20) NOT NULL,
  `CODE` varchar(20) NOT NULL,
  `SCLAS_CODE` varchar(20) NOT NULL,
  `SDCLAS_CODE` varchar(20) NOT NULL,
  `SDCLAS_NM` varchar(200) NOT NULL,
  `ATTRB_1` varchar(1000) DEFAULT NULL,
  `ATTRB_2` varchar(1000) DEFAULT NULL,
  `ATTRB_3` varchar(1000) DEFAULT NULL,
  `SORT_ORDR` int DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT NULL,
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  `UPDUSR_NO` int DEFAULT NULL,
  `UPDT_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`,`CODE`,`SCLAS_CODE`,`SDCLAS_CODE`)
)