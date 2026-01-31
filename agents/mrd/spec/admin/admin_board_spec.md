# Admin Board Specification

## 1. 개요 (Overview)

- **목적**: `admin/mgt/board` 영역의 게시판 모듈 구현 및 데이터 관리.
- **기준**: 패키지 구조 및 폴더 레이아웃은 `tech_spec.md`를 엄격히 준수함.
- **데이터 모델**: `/src/main/resources/schema/admin/mgt/board/schema.sql` 참조.

## 2. URL 매핑 규칙 (URL Mapping)

모든 게시판 관련 요청은 아래의 엔드포인트를 사용한다.

| 기능      | URL                                        | 설명                                 |
| :-------- | :----------------------------------------- | :----------------------------------- |
| 목록 조회 | `/admin/mgt/mboard/selectPageListBoard.do` | 페이징 처리가 포함된 리스트 조회     |
| 등록 폼   | `/admin/mgt/mboard/insertFormBoard.do`     | 게시글 작성 화면 이동                |
| 상세 조회 | `/admin/mgt/mboard/selectBoard.do`         | 단건 상세 내용 및 첨부파일 목록 조회 |
| 수정 폼   | `/admin/mgt/mboard/updateFormBoard.do`     | 기존 게시글 수정 화면 이동           |

## 3. 데이터베이스 및 연관관계 (Data Model)

### 3.1 테이블: tb_board (주요 컬럼)

- `BBS_SE_CODE_L`: 게시판 대분류 코드 (Default: `R010170`)
- `BBS_SE_CODE_M`: 게시판 중분류 코드
- `ATCH_YN`: 첨부파일 핸들링 옵션
  - `Y`: 다운로드 모드 (클릭 시 파일 다운로드 창 표시)
  - `N`: 뷰어 모드 (이미지 등 콘텐츠를 화면에 즉시 노출)

### 3.2 테이블: tb_file (주요 컬럼 및 로직)

- `DOC_ID`: 문서 그룹 ID (`tb_board.ATCH_DOC_ID`와 Join)
- `FILE_ID`: 물리 파일 식별자. 링크 생성 시 `FILE_ID + FILE_EXTSN_NM` 조합 사용 (파일명 깨짐 방지).
- `FILE_NM`: 사용자가 업로드한 원본 파일명 (화면 표시용).

### 3.3 연관관계 (Relationship)

- **Join 조건**: `tb_board.ATCH_DOC_ID = tb_file.DOC_ID`
- **정렬**: `tb_file.SORT_ORDR` 오름차순으로 파일 목록을 출력한다.

## 4. 파일 첨부 구현 가이드 (File Upload)

### 4.1 환경 설정 (application-local.yml)

파일 경로 및 용량 제한은 아래 설정을 참조한다.

```yaml
file:
  max-size-total: 50MB
  max-size-each: 10MB
  board:
    upload-dir: /Users/soo/uploads/board
    public-url: http://127.0.0.1:9000/common/img/board
    resource-path: file:///Users/soo/uploads/board/
```

### 4.2 유틸리티 및 비즈니스 로직

- **사용 유틸**: `FilePathResolver.java`, `AtFileMngUtil.java`, `SysUtil.java`
- **ID 생성 규칙**:
  - `DOC_ID`: `SysUtil.getDocId()` 메서드 호출.
  - `FILE_ID`: `SysUtil.getFileId()` 메서드 호출.
- **구현 방식**: `BoardService.insertBoard` 메서드 내부에서 파일을 먼저 저장하고, 반환된 `DOC_ID`를 `tb_board` 테이블에 매핑하여 저장한다.

## 5. 화면 구성 및 UI (View Structure)

- **UI 참조**: 제공된 첨부 이미지를 기반으로 Vue 3 + Bootstrap 5 레이아웃을 구성한다.
- **리스트 영역**: 대/중분류 필터링 및 페이징 내비게이션 포함.
- **파일 영역**: `ATCH_YN` 값에 따라 다운로드 버튼 또는 이미지 프리뷰 컴포넌트를 분기 처리한다.

## 6. 소스 생성 경로 (Source Generation)

AI는 아래 경로 이외의 위치에 소스를 생성하지 않는다.

- **Java**: `com.whomade.planfAi.admin.mgt.board`
  - `controller/`, `service/`, `vo/`, `mapper/`
- **XML**: `src/main/resources/mappers/admin/mgt/board/board_sql.xml`
- **Script**: `src/main/resources/schema/admin/mgt/board/schema.sql`
