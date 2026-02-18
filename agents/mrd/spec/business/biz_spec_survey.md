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

7. 설문 디자인 및 시각적 설정 요구사항
   설문 제작자가 브랜드 이미지에 맞게 설문의 외형을 커스텀 설정하고, 미리 정의된 테마를 적용할 수 있는 기능을 제공한다.

7.1 시각적 테마 및 레이아웃 설정
테마 선택 (THEME_SEQ): 시스템에서 제공하는 디자인 프리셋(예: 고층빌딩, 파스텔 등)을 선택할 수 있어야 하며, 선택 시 해당 테마의 기본 이미지가 미리보기에 반영되어야 한다.

레이아웃 유형 관리 (LAYOUT_TYPE): 설문지의 전체적인 배치를 설정한다. (중앙 정렬, 위쪽 배너형, 왼쪽/오른쪽 분할형 등)

구분선 스타일 (DIVIDER): 문항과 문항 사이를 구분하는 선의 종류 및 스타일을 설정하여 가독성을 높인다.

7.2 브랜드 로고 및 푸터 설정
로고 업로드 및 배치 (LOGO_IMAGE_PATH, LOGO_ALIGN): 사용자 정의 로고(회사 로고 등)를 업로드하고, 이를 설문 상단의 왼쪽, 가운데, 오른쪽 중 원하는 위치에 정렬할 수 있어야 한다.

서비스 로고 노출 제어 (SHOW_FOOTER_LOGO): 설문 하단(푸터) 영역에 시스템 기본 서비스 로고(아이콘)를 노출할지 여부를 선택할 수 있다.

7.3 상세 스타일 커스터마이징
색상 및 폰트 제어 (BGCOLOR, TEXTCOLOR, ACCENT_COLOR, FONT_FAMILY, FONT_SIZE): \* 배경색, 기본 글자색뿐만 아니라 버튼이나 포인트 요소에 사용될 **강조 색상(Accent Color)**을 개별 지정할 수 있다.

폰트의 종류(Family)와 크기(Size)를 설정하여 브랜드 아이덴티티를 유지한다.

테두리 설정 (SHOWBORDER): 설문지 본문 영역에 외곽 테두리를 표시하여 배경과 분리할지 여부를 설정한다.

7.4 고급 편집 및 확장 기능
HTML 헤더/푸터 (HEADER, FOOTER): 단순 텍스트가 아닌 HTML 태그를 직접 입력하여 설문 상/하단에 안내 이미지, 링크, 커스텀 안내문을 삽입할 수 있다.

사용자 정의 CSS (USERCSS): 제공되는 설정 UI 외에 정교한 스타일링이 필요한 경우, 직접 CSS 코드를 입력하여 설문지 전체 디자인을 오버라이드(Override) 할 수 있다.

경로 및 리소스 관리 (BASEHREF): 이미지나 외부 리소스 호출 시 상대 경로 참조를 위한 기준 URL을 설정한다.

💡 개발 구현 시 참고 로직 (참고용)
우선순위: THEME_SEQ로 선택된 기본 테마 값이 로드된 후, BGCOLOR, TEXTCOLOR 등 개별 필드에 저장된 값이 있다면 해당 값이 테마 값을 덮어쓰도록(Override) 구현한다.

미리보기: 관리자 페이지 내에서 디자인 변경 시, 우측 영역에 실시간으로 반영되는 'Live Preview' 기능을 구현하여 사용자 편의성을 극대화한다.

7-5 설문지 외형 설정
설문 제작자가 설문의 시각적 요소와 구조적 배치를 자유롭게 구성하여 브랜드 정체성을 반영하고 응답률을 높일 수 있도록 지원한다.

7-5-1. 테이블 구성
[신규 마스터 테이블] tb_survey_theme
사용자가 선택할 수 있는 디자인 프리셋(Pre-set) 목록을 관리하는 테이블이다.
resources/schema/admin/mgt/survey/schema.sql tb_survey_theme 테이블을 참고한다.
7-5-2. tb_survey 테이블 추가/변경 컬럼 상세
기존 스타일 필드 외에, 고도화된 설문 설정을 위해 아래 필드들을 추가 및 확장한다.
resources/schema/admin/mgt/survey/schema.sql tb_survey 테이블을 참고한다.

① 시각적 테마 및 레이아웃 관련
THEME_SEQ (INT): tb_survey_theme와 연결되는 외래키. 사용자가 선택한 기본 테마 세트를 결정한다.

LAYOUT_TYPE (VARCHAR): 설문지 배치 구조. (값 예시: CENTER, LEFT_1_3, RIGHT_1_2, TOP_BANNER)

SHOWBORDER (CHAR): 설문 본문 영역의 테두리 노출 여부 (이미지 속 '테두리 표시' 스위치).

DIVIDER (VARCHAR): 질문 문항 간 구분선의 디자인 스타일(실선, 점선, 여백 등).

② 로고 및 브랜드 설정 (Branding)
LOGO_IMAGE_PATH (VARCHAR): 사용자 업로드 로고 이미지 경로.

LOGO_ALIGN (VARCHAR): 로고 정렬 위치 (LEFT, CENTER, RIGHT).

SHOW_FOOTER_LOGO (CHAR): 하단 푸터 영역에 시스템/회사 로고 노출 여부.

③ 상세 스타일 (Custom CSS/Font)
FONT_FAMILY (VARCHAR): 설문에 적용할 서체 명칭.

ACCENT_COLOR (VARCHAR): 버튼, 체크박스 등 상호작용 요소에 적용될 강조 색상.

USERCSS (TEXT): 사용자가 직접 입력하는 오버라이딩 CSS 코드. (기존 VARCHAR에서 TEXT로 확장 권장)

④ 콘텐츠 및 종료 페이지
HEADER / FOOTER (TEXT): 상/하단 안내 문구. HTML 태그 삽입을 허용하여 이미지나 링크 구성이 가능하도록 한다.

EXITPAGETEXT (TEXT): 설문 완료 시 노출되는 문구.

EXITPAGETEXT_ISHTML (CHAR): 완료 문구의 HTML 렌더링 여부.

7-5-3. 주요 구현 로직 요구사항
테마-개별 스타일 우선순위: THEME_SEQ를 선택하면 해당 테마의 기본값이 로드되지만, 사용자가 BGCOLOR나 TEXTCOLOR를 직접 수정할 경우 개별 설정값이 테마값보다 우선하여 적용되어야 한다.

실시간 미리보기(Live Preview): 관리자 화면에서 로고를 업로드하거나 레이아웃을 바꿀 때, 우측 미리보기 패널에 즉시 반영되어야 한다.

반응형 레이아웃: LAYOUT_TYPE이 LEFT_1_3 등으로 설정되더라도, 모바일 기기 접속 시에는 자동으로 1컬럼(중앙 정렬)으로 전환되는 반응형 로직이 포함되어야 한다.

7-5-4. 몽키서베이 설문지 생성 에서 테마별로 설문지 생성 과 스타일 적요을 참조한다.

8.설문 배포 및 응답 수집 설계
8-1. 설문 오픈 및 접속 경로 설계
설문 상태가 **'수집중(OPEN)'**으로 변경되는 시점을 기준으로 서비스의 흐름이 전환됩니다.

접속 경로 (URL)
엔드포인트: https://{domain}/survey/v/{SURVEY_ID}

{domain} 은 application.yam의 survey.domain을 사용한다.

접근 제어: \* 상태가 수집중일 때만 페이지 렌더링 허용.
작성중이나 종료 상태일 경우 "접근할 수 없는 설문입니다" 안내 페이지로 리다이렉트.

비로그인 사용자 식별 전략
RESULTS_ID 발급: 사용자가 설문 페이지에 접속하거나 제출하는 시점에 고유한 고유 번호(Sequence 또는 UUID 기반 정수)를 생성합니다.

중복 방지 (선택 사항): \* 브라우저 LocalStorage에 survey*completed*{SURVEY_ID}: true 값을 저장하여 재참여를 방지합니다.

8-2. 응답 데이터 저장 구조 (Data Flow)
사용자가 설문 완료 후 저장 버튼을 클릭하면, 데이터는 질문 유형에 따라 두 가지 테이블로 분기되어 저장됩니다.

A. 기본 응답 내역 (tb_results)
역할: 설문의 핵심 답변(주관식, 단일 선택, 미디어 데이터)을 저장하는 마스터 테이블.

주요 데이터 필드:
RESULTS_ID: 한 명의 응답자를 식별하는 키 (모든 응답 묶음의 공통 ID).
QUESTION_RESULT: 주관식 입력 텍스트 또는 단일 선택된 값 저장.
inputImage: 서버에 저장된 이미지 경로 저장.
inputLocation: 위경도 정보를 구분자(예: ,)를 사용하여 저장.
OTHERANSWER_RESULT: '기타' 항목을 선택하고 직접 입력한 텍스트 저장.
REGISTER_NO: 비로그인 참여이므로 NULL 처리.

B. 상세 라벨 응답 (tb_results_label)
역할: 다중 선택(Checkbox)이나 매트릭스형 질문처럼 하나의 질문에 여러 응답이 발생하는 경우 처리.

주요 데이터 필드:
QUESTION_LABEL_ID: 선택된 각 보기(옵션)의 고유 ID.
QUESTION_LABEL_RESULT: 해당 옵션에 대한 값 (예: 'Y', '1', '5점' 등) 저장.
RESULTS_ID: tb_results와 동일한 ID를 사용하여 데이터 매핑.

8-3. 통계 추출 설계 (MRD 8번 기준)
수집된 데이터를 바탕으로 관리자 화면에서 실시간 리포트를 생성하며, 데이터 성격에 따라 다음과 같은 시각화 방식을 적용합니다.
통계 유형,활용 테이블,데이터 처리 및 시각화 방식
객관식 결과,tb_results_label,QUESTION_LABEL_ID별 카운트를 합산하여 파이(Pie) 또는 바(Bar) 차트 생성
주관식 결과,tb_results,QUESTION_RESULT의 텍스트를 추출하여 응답 리스트 또는 워드클라우드 노출
미디어 결과,tb_results,inputImage 경로를 통한 갤러리 뷰 및 inputLocation 기반 지도 마커 표시
기타 의견,tb_results,OTHERANSWER_RESULT에 값이 있는 경우 별도의 비고란 리스트 제공
8-4. 업무 프로세스 및 단계별 흐름 (Flow)
[Level 1] 관리자: 설문 오픈
설정 화면에서 상태를 **'수집중(OPEN)'**으로 변경 후 저장합니다.
시스템은 해당 SURVEY_ID를 포함한 **외부 접속용 URL(Short URL)**을 생성합니다.

[Level 2] 사용자: 설문 참여
사용자는 로그인 없이 제공된 URL에 접속합니다.
설문 문항을 작성하고 저장 버튼을 클릭합니다.

[Level 3] 서버: 데이터 적재
새로운 RESULTS_ID를 생성하여 응답 세션을 식별합니다.
단답형/주관식/이미지/위치 정보는 **tb_results**에 INSERT 합니다.
다중 선택형(객관식)은 선택된 개수만큼 **tb_results_label**에 INSERT 합니다.

[Level 4] 관리자: 통계 확인 (MRD)
관리자 화면에서 저장된 데이터를 실시간으로 집계하여 통계 대시보드에 노출합니다.
