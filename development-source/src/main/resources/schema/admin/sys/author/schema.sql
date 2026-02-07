
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

-- 기본 생성 Sql문 
-- 1. 권한 마스터에 관리자 권한 추가 (존재하지 않을 경우 대비)
INSERT INTO `op_author` (`AUTHOR_ID`, `AUTHOR_NM`, `RM`, `REGIST_DT`)
VALUES ('ROLE_ADMIN', '시스템 관리자', '전체 관리 권한', NOW());

