# [PRD] 소상공인 수익 분석 시스템 (Project: PlanfAi)

## 1. 프로젝트 개요

- **프로젝트명**: PlanfAi (소상공인 수익 분석 및 리포트 보관 서비스)
- **기본 패키지**: `com.whomade.planfAi`
- **목적**: 사용자가 입력한 매출/비용 데이터를 프론트엔드에서 즉시 계산하고, 그 결과물(JSON)을 서버에 저장하여 추후 관리할 수 있도록 함.

## 2. 기술 스택 (Technical Stack)

### Backend (Spring Boot)

- **Build Tool**: Maven
- **Framework**: Spring Boot 3.4.1
- **Database**: MySQL 8.x
- **Persistence**: MyBatis (XML Mapper 방식)
- **Utilities**: Lombok (Getter/Setter 자동화)
- **JDK 버전**: 21
- **Configuration**: `application-dev.yml` (또는 `application.yml`)

### Frontend (Vue.js)

- **Framework**: Vue 3 (CDN 방식)
- **UI Library**: Bootstrap 5 (CDN)
- **Icons**: Bootstrap Icons (CDN)
- **Charts**: Chart.js (CDN)
- **HTTP Client**: Axios (CDN)
- **특이사항**: 별도의 빌드 과정(npm/vite) 없이 브라우저에서 직접 실행되는 Single File 구조 활용.

## 3. 프로젝트 구조 (Standard Directory Layout)

```text
src/main/java/com/whomade/planfAi/
├── PlanfAiApplication.java
├── front/                         # [이동] 사용자 서비스 도메인
│   └── profitReport/              # 수익 리포트 관련 업무
└── admin/                         # 관리자 프레임워크 (System + Ops)
    ├── user/ | author/ | authmenu/ | menu/ | code/ ... (시스템 관리)
    └── mgt/                       # [신규] 운영 관리 업무 영역
        └── board/                 # 게시판 관리 (Target)
            ├── controller/
            ├── service/
            └── vo/

src/main/resources/
├── mappers/
│   ├── front/                     # [이동] 사용자 서비스 매퍼
│   │   └── profitReport/
│   └── admin/                     # 관리자 전용 매퍼
│       ├── user/ | code/ ...
│       └── mgt/
│           └── board/             # board_sql.xml (MyBatis SQL)
├── static/
│   ├── common/                    # 공통 에셋
│   ├── admin/                     # 관리자 전용 에셋
│   └── index.html
├── schema/                        # SQL DDL (AI 참조용)
│   ├── front/
│   ├── admin/
│   └── mgt/                       # tb_board.sql, tb_file.sql
├── application-dev.yml            # 설정 정보 (아래 2.1 참고)
└── pom.xml                        # 의존성 관리 (아래 2.2 참고)
```

## 4. 상세 요구사항

### 4.1. 데이터 처리 로직

- **Client-Side Logic**: 모든 수익 계산(매출 - 고정비 - 변동비 = 순이익) 및 경영 진단 로직은 Vue.js 내에서 처리함.
- **Server-Side Logic**: 서버는 복잡한 비즈니스 연산을 수행하지 않으며, 프론트엔드에서 완성된 JSON 데이터를 받아 DB에 저장하거나 요청 시 반환하는 데이터 허브 역할을 수행.

### 4.2. Database Schema (MySQL)

```sql
-- 사용자 테이블
CREATE TABLE IF NOT EXISTS tb_user (
    user_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_name VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 수익 분석 리포트 테이블
CREATE TABLE IF NOT EXISTS profit_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_no BIGINT NOT NULL,
    report_data JSON,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_no FOREIGN KEY (user_no) REFERENCES tb_user(user_no) ON DELETE CASCADE
);
```

### 4.3. MyBatis 설정 (`application-dev.yml`)

```yaml
mybatis:
  type-aliases-package: com.whomade.planfAi.profitReport.vo
  mapper-locations: classpath:mappers/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
```

## 5. 핵심 API 명세

- **리포트 저장 (Save)**: `POST /api/report`
  - Payload: `{ "userId": "string", "content": { ...계산된결과JSON... } }`
- **리포트 목록 조회 (Fetch)**: `GET /api/report/{userId}`
  - Response: 저장된 리포트 리스트 (JSON 배열)

## 6. 개발 지시사항

1.  **패키지 구조 엄수**: 라이브러리를 제외한 모든 Java 클래스는 `com.whomade.planfAi` 하위 패키지에 위치해야 함.
2.  **UI 엔진**: `index.html` 내부에 Vue.js 라이브러리를 CDN으로 로드하여 사용하며, 별도의 npm 빌드 도구를 사용하지 않음.
3.  **SQL 분리**: 모든 SQL 쿼리는 MyBatis XML Mapper 파일에 작성하여 Java 코드와 명확히 분리함.
4.  **공통 자원 관리**: 공통으로 사용되는 스타일과 스크립트는 `static/common` 폴더의 `common.css`, `common.js`에서 관리함.
