1. 시스템 개요 (System Overview)
   목적: 효율적인 시스템 운영을 위한 사용자 권한(RBAC), 계층형 메뉴, 공통 코드 관리 시스템 구축.

물리 구조 참조 (Standard Directory Layout Detail): AI는 모든 파일 생성 시 아래의 [도메인별 경로 매핑] 규칙을 엄격히 준수한다.

핵심 규칙: 모든 MyBatis 매퍼 XML은 mappers/admin/ 하위 도메인 폴더에 위치하며, 모든 테이블 설계는 schema/admin/ 폴더 내 DDL 파일을 최우선 참조한다.
tech_spec.md의 프로젝트 구조 (Standard Directory Layout) 참조

[도메인별 경로 매핑]

도메인 Java 패키지 MyBatis 매퍼 위치 테이블 DDL 위치
사용자 관리 admin.user mappers/admin/user/
schema/admin/op_user.sql
권한 관리 admin.author mappers/admin/author/
schema/admin/op_author.sql
권한-메뉴 admin.authmenu mappers/admin/authmenu/
schema/admin/op_author_menu.sql
메뉴 관리 admin.menu mappers/admin/menu/
schema/admin/op_menu.sql
코드 관리 admin.code mappers/admin/code/
schema/admin/op_code_group.sql, op_code.sql
소분류 코드 admin.sccode mappers/admin/sccode/
schema/admin/op_sclas_code.sql 없으면 생성
세분류 코드 admin.sdcode mappers/admin/sdcode/
schema/admin/op_sdclas_code.sql 없으면 생성
기본 규칙:

모든 리스트 및 트리 구조는 SORT_ORDR 필드를 최우선으로 정렬한다.

USE_YN 필드가 'N'인 데이터는 기능 및 UI 노출에서 제외한다.

2. 메뉴 관리 (Menu Management)
   사용자가 직접 메뉴 ID의 하위 4자리를 입력하여 계층을 생성하는 방식을 채택한다.

2.1 메뉴 ID 생성 및 레벨 규칙
ID 구조: 4자리 단위 고정 길이 확장 방식.

Level 1: 1000

Level 2: 10001000 (부모 1000 + 사용자 입력 1000)

로직 위치: admin.menu 패키지 및 mappers/admin/menu/ 참조.

자동 계산: MENU_LEVEL = (MENU_ID 전체 길이 / 4).

유효성 검사: 중복 체크 시 상위 ID와 입력값 4자리를 결합하여 검증하며, 오직 숫자 4자리만 허용한다.

2.2 정렬 로직 (Denormalized Sort)
계층적 순서 보장을 위해 SORT_ORDR_1 ~ SORT_ORDR_6 필드를 사용한다.

상속 규칙: 신규 등록 시 부모의 모든 SORT_ORDR_n 값을 복사한다.

저장 규칙: 현재 본인의 레벨(MENU_LEVEL)에 해당하는 필드에만 새로운 순번을 저장한다.

3. 권한 및 접근 제어 (RBAC)
   권한-메뉴 매핑: op_author_menu 테이블을 사용하여 권한 그룹별 가용 메뉴 트리를 구성한다.

접근 통제: 로그인 사용자의 권한을 세션/토큰에서 확인하여, 권한 없는 URL 접근은 서버단에서 차단한다.

물리 참조: admin.author, admin.authmenu 패키지 참조.

4. 계층형 코드 관리 (Code System)
   시스템 공통 코드(대/중/소/세)를 통합 관리하며, admin.code, admin.sccode, admin.sdcode 패키지에서 처리한다.

조회: GROUP_ID 기반 트리 구조 호출.

매핑: 업무 데이터 조회 시 코드명(CODE_NM)을 매핑하여 노출한다.

5. 사용자 관리 (User Management)
   보안: 비밀번호는 BCrypt 알고리즘을 사용하여 단방향 암호화 후 PASSWORD 필드에 저장한다.

상태: USER_STTUS_CODE가 '정상'인 사용자만 로그인을 허용한다.

물리 참조: admin.user 패키지 및 schema/admin/op_user.sql 참조.
