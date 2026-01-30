-- 게시판 테이블 생성
-- 운영 게시판 테이블 생성 (BBS_SEQ 자동 증가 추가)
CREATE TABLE `tb_board` (
  `BBS_SEQ` bigint NOT NULL AUTO_INCREMENT COMMENT '게시글 고유번호',
  `BBS_SE_CODE_L` varchar(20) NOT NULL COMMENT '게시판 대분류 코드',
  `BBS_SE_CODE_M` varchar(20) NOT NULL COMMENT '게시판 중분류 코드',
  `SJ` varchar(200) NOT NULL COMMENT '제목',
  `CN` text COMMENT '내용',
  `ATCH_DOC_ID` varchar(45) DEFAULT NULL COMMENT '첨부파일 문서 ID',
  `HIT_CNT` bigint DEFAULT 0 COMMENT '조회수',
  `ATCH_YN` varchar(1) DEFAULT 'N' COMMENT '첨부파일 다운로드 여부 (Y/N)',
  `DELETE_YN` varchar(1) DEFAULT 'N' COMMENT '삭제 여부 (Y/N)',
  `REGISTER_NO` int DEFAULT NULL COMMENT '등록자 번호',
  `REGIST_DT` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
  `UPDUSR_NO` int DEFAULT NULL COMMENT '수정자 번호',
  `UPDT_DT` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  PRIMARY KEY (`BBS_SEQ`)
) COMMENT='운영 게시판 테이블';

-- 첨부파일 테이블 생성
CREATE TABLE `tb_file` (
  `FILE_SEQ` bigint NOT NULL AUTO_INCREMENT COMMENT '파일 고유번호',
  `DOC_ID` varchar(45) NOT NULL COMMENT '문서 그룹 ID (tb_board.ATCH_DOC_ID와 매핑)',
  `FILE_ID` varchar(45) NOT NULL COMMENT '파일 식별 ID',
  `FILE_NM` varchar(200) NOT NULL COMMENT '원본파일명',
  `FILE_EXTSN_NM` varchar(10) DEFAULT NULL COMMENT '파일 확장자',
  `FILE_SIZE` varchar(100) DEFAULT NULL COMMENT '파일 크기',
  `FILE_PARTN_COURS` varchar(200) DEFAULT NULL COMMENT '파일 상대 경로',
  `FILE_ABSLT_COURS` varchar(200) DEFAULT NULL COMMENT '파일 절대 경로',
  `CNTNTS_TY` varchar(200) DEFAULT NULL COMMENT '컨텐츠 타입 (MIME)',
  `SORT_ORDR` int DEFAULT 0 COMMENT '정렬 순서',
  `REGISTER_NO` int DEFAULT NULL COMMENT '등록자 번호',
  `REGIST_DT` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
  `UPDUSR_NO` int DEFAULT NULL COMMENT '수정자 번호',
  `UPDT_DT` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  PRIMARY KEY (`FILE_SEQ`),
  KEY `IDX_TB_FILE_DOC_ID` (`DOC_ID`)
) COMMENT='운영 첨부파일 테이블';