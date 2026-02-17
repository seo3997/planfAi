문 웹 서비스 데이터 모델 및 관리 정책 (MRD)
본 문서는 설문 시스템의 객체 구조, 데이터베이스 매핑 관계 및 운영 정책을 정의합니다.

1. 소스 코드 및 화면 설계 참조 (Resource Reference)
   1-1. 화면 설계 참조 (UI/UX Design)
   설계서 및 리소스 정보
   설계서 위치: docs/admin/mgt/survey/design/ 폴더 내 이미지 참조

개발 가이드: 모든 HTML 렌더링 및 UI 컴포넌트는 아래 정의된 설계 이미지와 호출 URL을 기준으로 개발을 진행합니다.

🖼️ 주요 참조 이미지 및 호출 URL 매핑
설문 생성, 수집, 결과 분석 등 각 기능별 화면 UI 구성은 해당 폴더 내의 스크린샷 파일을 기준으로 개발합니다.

1-1-1. 설문 리스트 관리
파일명: 00.설문리스트관리\_01~02.png
호출 URL: /mgt/survey/selectPageListSurvey.do
기능 설명: 전체 설문 목록 조회 및 신규 설문 등록

1-1-2. 설문 마스터 관리
파일명: 01.설문마스타관리.png
호출 URL: /mgt/survey/manageSurveyMenu.do?surveyId={id}
기능 설명: 특정 설문의 마스터 설정 및 관리 메뉴

1-1-3. 설문 양식 등록 (기본)
파일명: 02.설문양식등록.png
호출 URL: /mgt/survey/editEntryForm.do?surveyId={id}
기능 설명: 설문 기본 정보, 섹션 설정 및 문항 구성 편집

1-1-4. 질문 유형 선택
파일명: 03.설문양식등록\_질문유형선택.png
호출 방식: 팝업 또는 모달 레이어 호출
기능 설명: 질문 타입(Radio, Checkbox 등) 선택 UI

1-1-5. 상세 입력 타입 설정 (InputType)
파일명: 04.설문양식등록\_01~07.png
호출 방식: 각 유형별 상세 설정 컴포넌트
기능 설명: 상세 속성 설정 (Checkbox, Radio, Textline, Textarea, Image, Location, Comment)

1-2 Java 소스 코드 (Business Logic)
참조 경로: src/main/resources/docs/admin/mgt/survey/sample/
핵심 클래스:
iSurveyEntryForm.java: 설문 전체 폼 관리 로직
Question.java 및 하위 클래스: 각 질문 타입별(InputRadio, InputTextline 등) 데이터 처리 로직

1-3 데이터 및 렌더링 정책 (JSON & Template)

옛날 방식의 XML 처리를 배제하고 JSON 기반의 데이터 구조와 템플릿 엔진을 사용합니다.

JSON 데이터 표준: 설문 구조(Section, Question) 및 답변 데이터는 모두 JSON 포맷으로 직렬화하여 처리합니다.

관심사 분리 (SoC): 자바 소스(sample/\*.java) 내부에 하드코딩된 HTML 생성 로직을 제거합니다.
템플릿 엔진 활용: 자바는 JSON 데이터를 생성하여 Model에 담고, 실제 HTML은 mgt/survey/template/ 경로의 템플릿 파일에서 렌더링합니다.

💡 참고 가이드

모든 개발은 tech_spec.md의 프로젝트 구조를 준수하며, 각 리소스 폴더의 mgt/survey를 기준으로 진행하십시오.

각 질문 타입별 상세 로직은 자바 클래스(InputRadio.java, InputTextline.java 등)와 매핑되어야 합니다.

2. 설문 마스터 관리 정책 (Lifecycle)
   설문은 다음의 3단계 상태로 관리되며, 단계별 권한과 기능이 제어됩니다.
   준비단계 (Create): 설문 제목, 섹션, 문항 및 옵션을 구성하는 단계입니다.

설문시작/설문종료 (Collect): OPENED 상태가 활성화되어 사용자가 설문에 참여하고 답변을 제출할 수 있는 실시간 서비스 단계입니다.

설문결과분석 (Close): CLOSED 상태 또는 종료 일시 도달 시 답변 제출이 차단되며, 수집된 데이터의 결과 분석이 가능해지는 단계입니다.

3. 테마 및 디자인 적용 (Theme Policy)
   동적 테마: tb_survey에 설정된 BGCOLOR, TEXTCOLOR, FONT_SIZE 등을 기반으로 각 설문마다 고유한 테마가 적용됩니다.

일관성: 모든 질문 객체(Question.java)는 상속받은 테마 설정값을 사용하여 HTML 컴포넌트를 생성함으로써 전체 설문의 디자인 일관성을 유지합니다.

4. 데이터베이스 및 객체 매핑 상세
   상세 DDL은 resources/schema/admin/mgt/survey/schema.sql을 참조하십시오.

이 문서는 설문 웹 서비스의 객체 구조와 데이터베이스 매핑 관계를 정의합니다. 상세한 테이블 생성 스크립트는 프로젝트 내 resources/schema/admin/mgt/survey/schema.sql 파일을 참조하십시오.

5.업무 설명
5.1. 설문 마스터 (Master)
관련 테이블: tb_survey

Java 객체: iSurveyEntryForm.java

설명: 설문의 전역 설정, 디자인(색상, 폰트), 보안(비밀번호), 종료 페이지 문구 등을 관리합니다.

주요 매핑:

SURVEY_ID ↔ id

SURVEY_TITLE ↔ title

BGCOLOR ↔ bgColor

EXITPAGETEXT ↔ exitPageText

5.2. 설문 섹션 (Section)
관련 테이블: tb_section

Java 객체: iSection.java

설명: 설문 내의 페이지 구분 또는 질문 그룹 단위입니다. 하나의 설문은 여러 개의 섹션을 가질 수 있습니다.

주요 매핑:

SECTION_ID ↔ sectionId

SECTION_TITLE ↔ title

5.3. 설문 질문 (Question)
관련 테이블: tb_question

Java 객체: Question.java (추상 클래스 및 하위 타입)

설명: 실제 설문 문항입니다. QUESTION_TYPE 컬럼 값에 따라 자바의 하위 클래스가 결정됩니다.

유형별 매핑 클래스:

inputRadio: 단일 선택형 (InputRadio.java)

inputCheckbox: 다중 선택형 (InputCheckbox.java)

inputTextline: 단답형 입력 (InputTextline.java)

inputTextarea: 서술형 입력 (InputTextarea.java)

inputImage: 이미지 업로드 (InputImage.java)

inputLocation: 지도 위치 정보 (InputLocation.java)

inputComment: 안내 텍스트/HTML (InputComment.java)

5.4. 질문 라벨 (Question Label)
관련 테이블: tb_question_label

Java 객체: Option.java / OptionList.java

설명: 객관식 질문(Radio, Checkbox)에서 사용자가 선택할 수 있는 보기 항목들입니다.

주요 매핑:

QUESTION_LABEL ↔ optionText

SELECTED ↔ selected

💡 개발 참고사항

DDL 참조: 테이블의 PK, FK 제약조건 및 인덱스 정보는 schema/admin/mgt/survey/schema.sql을 참조하십시오.

공통 컬럼: 모든 테이블은 REGISTER_NO, REGIST_DT, UPDUSR_NO, UPDT_DT 컬럼을 통해 이력을 관리합니다.

6. 기술스펙은 tech_spec.md 의 ## 3. 프로젝트 구조 (Standard Directory Layout)를 참고하십시오.
   각폴드별 mgt/survey 폴더를 기준으로 개발을 진행하십시오.
