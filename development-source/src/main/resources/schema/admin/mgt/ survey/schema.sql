1.설문
-- planfaidb.tb_survey definition
CREATE TABLE `tb_survey` (
  `SURVEY_SEQ` int NOT NULL AUTO_INCREMENT COMMENT '설문 일련번호 (PK)',
  `SURVEY_ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 고유 식별 ID',
  `SURVEY_TITLE` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '설문 제목',
  
  -- 설문 기간 및 운영 설정
  `OPENED` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 시작 일시',
  `CLOSED` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 종료 일시',
  `ADMINEMAIL` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '관리자 이메일',
  `ADMINPID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '관리자 개인 식별 ID',
  
  -- 접근 및 결과 제한 설정
  `ACCESSRESULTSRESTRICTION` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '결과 조회 제한 여부',
  `RESULTSPASSWORD` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '결과 확인용 비밀번호',
  `ENTRYRESTRICTION` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 참여 제한 방식',
  `ENTRYPASSWORD` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 참여 비밀번호',
  `MEMBERS` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '참여 허용 회원 그룹',
  `ONEENTRYONLY` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '중복 참여 제한 여부 (1인 1회)',
  
  -- 데이터 내보내기 및 로그 설정
  `EXPORTDELIMITER` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '데이터 내보내기 구분자',
  `EXPORTINCLUDEQUESTIONS` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '내보내기 시 질문 포함 여부',
  `IP` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '응답자 IP 수집 여부',
  `BROWSERID` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '응답자 브라우저 정보 수집 여부',
  
  -- 테마 및 스타일 설정 (이미지 관련 기능)
  `SHOWBORDER` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 영역 테두리 표시 여부',
  `BGCOLOR` varchar(7) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '전체 배경색 (HEX 코드)',
  `TEXTCOLOR` varchar(7) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '기본 글자색 (HEX 코드)',
  `FONT_SIZE` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '글꼴 크기 (단위 포함)',
  `DIVIDER` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '문항 간 구분선 스타일',
  
  -- HTML 및 사용자 정의 콘텐츠
  `HEADER` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 상단 헤더 HTML',
  `FOOTER` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 하단 푸터 HTML',
  `BASEHREF` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '상대 경로 참조용 베이스 URL',
  `USERCSS` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '사용자 정의 추가 CSS 스타일',
  
  -- 문구 및 완료 페이지 설정
  `NUMBERRODUCTIONTEXT` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '문항 번호 접두 문구',
  `EXITPAGETEXT` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '설문 종료 후 표시 문구',
  `EXITPAGETEXT_ISHTML` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '종료 문구 HTML 허용 여부',
  
  -- 시스템 기록
  `REGISTER_NO` int DEFAULT NULL COMMENT '등록자 식별 번호',
  `REGIST_DT` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '최초 등록 일시',
  `UPDUSR_NO` int DEFAULT NULL COMMENT '최종 수정자 식별 번호',
  `UPDT_DT` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '최종 수정 일시',
  
  PRIMARY KEY (`SURVEY_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

ALTER TABLE `tb_survey` 
  -- 테마 매칭 (이미지 1 관련)
  ADD COLUMN `THEME_SEQ` int DEFAULT NULL COMMENT '선택한 테마 일련번호',
  
  -- 로고 및 브랜드 (이미지 2 관련)
  ADD COLUMN `LOGO_IMAGE_PATH` varchar(500) DEFAULT NULL COMMENT '업로드 로고 파일 경로',
  ADD COLUMN `LOGO_ALIGN` varchar(10) DEFAULT 'LEFT' COMMENT '로고 정렬(LEFT, CENTER, RIGHT)',
  ADD COLUMN `SHOW_FOOTER_LOGO` char(1) DEFAULT 'Y' COMMENT '하단 서비스 로고 표시 여부',
  
  -- 상세 레이아웃 (이미지 3 관련)
  ADD COLUMN `LAYOUT_TYPE` varchar(20) DEFAULT 'CENTER' COMMENT '레이아웃 유형(CENTER, TOP_BANNER, LEFT_1_3 등)',
  
  -- 상세 스타일 보완
  ADD COLUMN `FONT_FAMILY` varchar(50) DEFAULT NULL COMMENT '적용 폰트 서체명',
  ADD COLUMN `ACCENT_COLOR` varchar(7) DEFAULT NULL COMMENT '강조 색상(버튼, 포인트 컬러)',


-- planfaidb.tb_section definition
CREATE TABLE `tb_section` (
  `SECTION_SEQ` int NOT NULL AUTO_INCREMENT,
  `SURVEY_ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `SECTION_ID` int NOT NULL,
  `SECTION_TITLE` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `SECTION_HTMLTITLE` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `REGISTER_NO` int DEFAULT NULL COMMENT '등록자 번호',
  `REGIST_DT` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
  `UPDUSR_NO` int DEFAULT NULL COMMENT '수정자 번호',
  `UPDT_DT` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  PRIMARY KEY (`SURVEY_ID`,`SECTION_ID`),
  UNIQUE KEY `SECTION_SEQ_UNIQUE` (`SECTION_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- planfaidb.tb_question definition

CREATE TABLE `tb_question` (
  `QUESTION_SEQ` int NOT NULL AUTO_INCREMENT,
  `SURVEY_ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `SECTION_ID` int NOT NULL,
  `QUESTION_ID` int NOT NULL,
  `QUESTION_TYPE` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `SHOWDIVIDER` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `EXPORTINCLUDE` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `EXPORTEXPAND` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `QUESTIONTEXT` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `QUESTION_CD` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `QUESTION_ISHTML` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `QUESTION_LAYOUT` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `QUESTION_LABEL` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `OTHERANSWER` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `TEXTCOLS` int DEFAULT NULL,
  `TEXTROWS` int DEFAULT NULL,
  `TEXTSIZE` int DEFAULT NULL,
  `MAXLENGTH` int DEFAULT NULL,
  `SUMMARY_CD` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '0',
  `REGISTER_NO` int DEFAULT NULL COMMENT '등록자 번호',
  `REGIST_DT` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
  `UPDUSR_NO` int DEFAULT NULL COMMENT '수정자 번호',
  `UPDT_DT` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  PRIMARY KEY (`QUESTION_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- planfaidb.tb_question_label definition
CREATE TABLE `tb_question_label` (
  `QUESTION_LABEL_SEQ` int NOT NULL AUTO_INCREMENT,
  `SURVEY_ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `SECTION_ID` int NOT NULL,
  `QUESTION_ID` int NOT NULL,
  `QUESTION_LABEL_ID` int NOT NULL,
  `QUESTION_LABEL` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `EXPORTCODE` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `SELECTED` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `REGISTER_NO` int DEFAULT NULL COMMENT '등록자 번호',
  `REGIST_DT` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
  `UPDUSR_NO` int DEFAULT NULL COMMENT '수정자 번호',
  `UPDT_DT` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  PRIMARY KEY (`QUESTION_LABEL_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- planfaidb.tb_survey_theme definition
CREATE TABLE `tb_survey_theme` (
  `THEME_SEQ` int NOT NULL AUTO_INCREMENT,
  `THEME_NAME` varchar(100) NOT NULL COMMENT '테마 명칭 (고층빌딩, 파스텔 등)',
  `THEME_IMAGE_PATH` varchar(500) DEFAULT NULL COMMENT '테마 미리보기 이미지(썸네일) 경로',
  `BG_IMAGE_PATH` varchar(500) DEFAULT NULL COMMENT '배경 이미지 경로',
  `BG_COLOR` varchar(20) DEFAULT '#FFFFFF' COMMENT '배경 색상 코드',
  `TEXT_COLOR` varchar(20) DEFAULT '#333333' COMMENT '텍스트 색상 코드',
  `ACCENT_COLOR` varchar(20) DEFAULT '#2b4b8a' COMMENT '강조 색상 코드',
  `LAYOUT_TYPE` varchar(20) DEFAULT 'CENTER' COMMENT '레이아웃 타입 (CENTER, TOP_BANNER 등)',
  `IS_ACCESSIBILITY` char(1) DEFAULT 'N' COMMENT '접근성 아이콘 표시 여부',
  `REGIST_DT` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`THEME_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample Themes with working Unsplash images for immediate preview
INSERT INTO `tb_survey_theme` (THEME_NAME, THEME_IMAGE_PATH, BG_IMAGE_PATH, BG_COLOR, TEXT_COLOR, ACCENT_COLOR, LAYOUT_TYPE, IS_ACCESSIBILITY)
VALUES 
('Standard White', '/common/images/themes/thumb_white.jpg', '', '#f8f9fa', '#212529', '#2b4b8a', 'CENTER', 'N'),
('Modern Skyscraper', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=400&auto=format&fit=crop', 'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070&auto=format&fit=crop', '#e9ecef', '#212529', '#0d6efd', 'CENTER', 'N'),
('Calm Raindrop', 'https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?q=80&w=400&auto=format&fit=crop', 'https://images.unsplash.com/photo-1515694346937-94d85e41e6f0?q=80&w=1974&auto=format&fit=crop', '#dee2e6', '#212529', '#198754', 'LEFT_1_3', 'N'),
('Elegant Walnut', 'https://images.unsplash.com/photo-1516233501032-2d03a129ef9b?q=80&w=400&auto=format&fit=crop', 'https://images.unsplash.com/photo-1516233501032-2d03a129ef9b?q=80&w=2070&auto=format&fit=crop', '#efebe9', '#3e2723', '#795548', 'CENTER', 'N');