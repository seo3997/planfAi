
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

