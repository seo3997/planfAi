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
  `AREA_SE_CODE_S` varchar(20) DEFAULT NULL COMMENT '지역 구분 코드 중',
  `AREA_SE_CODE_D` varchar(20) DEFAULT NULL COMMENT '지역 구분 코드 소',
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


-- 관리자 계정 생성 (비밀번호 1234 암호화 적용)
INSERT INTO `op_user` (
  `USER_ID`, 
  `PASSWORD`, 
  `USER_NM`, 
  `EMAIL`, 
  `AUTHOR_ID`, 
  `USER_STTUS_CODE`, 
  `REGIST_DT`
) VALUES (
  'admin', 
  '$2a$10$vI8tmZH7/SByyS.uU7.E/uzYDRSuxyt8Aa.dPlNV6JpS3mjKu6F9i', -- '1234' BCrypt Hash
  '관리자', 
  'seo3997@gmil.com', 
  'ROLE_ADMIN', 
  '정상', 
  NOW()
);

