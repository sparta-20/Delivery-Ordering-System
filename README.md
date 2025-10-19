# 🚚AI 검증 비즈니스 프로젝트 

## ✨ 프로젝트 개요
- 프로젝트명: '요기요저기요'
- 프로젝트 소개: '요기요저기요'는 음식점들의 배달 주문 관리와 주문 내역 관리 기능을 제공하는 음식 주문 관리 플랫폼입니다.
- 프로젝트 개발 기간: 2025.09.26 ~ 2025.10.20
 
## ✨ 개발환경 소개
| 분류 | 상세 | 
| --- | --- | 
| IDE | IntelliJ | 
| Language | Java21 | 
| Framework | Spring Boot 3.5.5 | 
| Repository | H2 In-memory, PostgreSQL | 
| Build Tool | Gradle | 
| DevOps - dev | EC2, RDS(PostreSQL), Docker, GihubActions, (Nginx) | 

## ✨ 설계 산출물
### 🎯 1. 도메인 다이어그램
<img width="768" height="393" alt="Group 2" src="https://github.com/user-attachments/assets/1a2fc472-901a-413d-9480-1b19d611463e" />

### 🎯 2. AWS 기반의 운영 환경 다이어그램
<img width="601" height="861" alt="제목 없는 다이어그램 drawio (3)" src="https://github.com/user-attachments/assets/b778ea4a-9663-4d8d-b6b6-422dac4026a2" />

### 🎯 3. Coding Conventions
#### 📌 1. 커밋 메시지 규칙
- refactor/기능명
- feat/기능명
- fix/버그 내용
- docs/문서수정
- test/테스트코드
#### 📌 2. Java Code Style
| 분류 | 상세 | 
| --- | --- | 
| 폴더명 | 소문자 + _(snake_case) | 
| 변수명 | 카멜케이스(camelCase) | 
| 함수명/메소드명 | 카멜케이스(camelCase) | 
| 클래스명 | 파스칼케이스(pascalCase) | 
| 상수명 | 대문자 + _(upper_snake_case) | 
| Enum | Enum Suffix(OrderTypeEnum) |

#### 📌 3. Git branch 전략
| 브랜치 | 상세 | 
| --- | --- | 
| main | 실제 서비스에서 배포되는 최종 버전을 관리하는 브랜치 | 
| dev | 모든 기능 브랜치가 통합되는 개발 메인 브랜치 | 
| feature | 새로운 기능이나 개선 사항을 개발하는 단위 작업 브랜치 | 
| hotfix | 긴급 버그를 신속히 수정하기 위한 브랜치 | 

## ✨ 개발 산출물
### 🎯 프로젝트 구조
```js
└── com
    └── delivery
        ├── domain
        │   ├── address
        │   │    ├── controller
        │   │    ├── dto
		│   │    ├── entity
		│   │    ├── repository
		│   │    └── service
        │   ├── ai
        │   ├── auth
        │   ├── cart
        │   ├── menu
        │   ├── order
        │   ├── review
        │   ├── store
        │   ├── test
        │   └── user
        └── global
            ├── common
            ├── config
            ├── exception
            ├── jwt
            ├── security
            └── util
```

### 🎯 주요 기능
#### 📌 1. 인증/인가 기능
 1. 회원가입: 비밀번호는 BCrypt로 암호화되어 저장
 2. 로그인: 검증에 성공하면 AcessToken과 RefreshToken 발급(AccessToken: 쿠키에 저장, refreshToken: DB에 저장)
 3. AccessToken이 만료되면 RefreshToken을 통해 새 토큰 재발급
 4. 로그아웃 시 AccessToken은 블랙리스트에 등록되고 RefreshToken은 삭제되어 사용 불가능
 5. 모든 요청은 JwtAuthorizationFilter를 거쳐 쿠키에 담긴 AccessToken 검증
#### 📌 2. 유저 기능
 1. Auth 과정에서 Seucrity에 의해 생성
 2. 자기 자신의 정보만 수정/삭제/조회 가능
 3. Admin api로 권한 변경 가능
#### 📌 3. 주소 기능
 1. 별칭 상세주소를 입력받아 새로운 배송지 등록
 2. 로그인한 사용자의 모든 주소를 조회
 3. 별칭과 상세주소를 입력받아 배송지 수정
 4. 사용자의 주소를 논리 삭제 방식으로 처리
 5. 특정 주소를 기본 주소로 설정 가능
#### 📌 4. 가게 기능
 1. OWNER는 가게를 생성/수정/삭제/본인 가게 목록 조회 가능
 2. MANAGER, MASTER는 가게의 모든 권한을 가짐
 3. MANAGER, MASTER는 점주별 가게 목록 조회 가능
 4. 광화문 근처(3km)에서 운영되는 가게 목록만 조회 가능(검색, 카테고리별, 지역별 조회)
 5. 현재 등록된 카테고리 목록 조회
#### 📌 5. 메뉴 기능
 1. 페이지네이션 목록 조회 제공
 2. 생성/ 수정/ 삭제/ 단일 조회 제공
 3. 권한별 기능 제공
#### 📌 6. 주문 기능
 1. 페이지네이션 목록 조회 제공
 2. 생성/ 수정/ 삭제/ 상태 변 제공
 3. 권한별 기능 제공
#### 📌 7. AI 기능
 1. 프롬프트에 의한 텍스트 자동 생성 기능을 지원
 2. 메뉴를 소유한 가게 사장(OWNER): 본인이 작성한 AI 기록에 대해 조회, 삭제, 검색이 가능
 3. 관리자(MANAGER, MASTER): 모든 AI 기록에 대해 조회, 삭제, 검색이 가능
#### 📌 8. 리뷰 기능
 1. 고객은 1~5점 평점과 리뷰 내용을 작성할 수 있으며, 모든 리뷰는 Soft Delete 방식으로 관리
 2. 중복 리뷰는 방지되며, 주문이 배송 완료 상태일 때만 작성 가능
 3. 권한에 따라 접근 범위 제한
 4. 리뷰 검색

## ✨ 트러블 슈팅
### 🎯 N+1 문제
#### 📌 문제상황
- Cart 1개를 조회할 때 CartItem의 개수만큼 추가 쿼리가 실행되는 구조였고, 이로 인해 성능 저하가 발생
#### 📌 해결방법과 기대효과
- Fetch 조인을 사용해 쿼리에서 Cart 기준으로 CartItem, Menu, Store를 한 번에 조인해 필요한 데이터를 모두 한 번의 쿼리로 가져옴
- 기대효과: 쿼리 호출 수를 크게 줄이면서 성능을 최적화

### 🎯 GeminiConfig — 파라미터 기반 빈 한계
#### 📌 문제상황
- 메뉴 설명 자동 생성에만 사용되던 AI 기능의 확장성을 고려하여 요청 타입별로 다른 설정을 관리할 필요성이 생김.
- 초기 구현은 요청이 들어올 때마다 요청 타입을 파라미터로 받아 GenerateContentConfig 객체를 새로 생성하는 방식이었지만 요청이 많아질수록 객체 생성 비용이 증가하는 비효율이 발생
- 이를 개선하기 위해 한 번만 생성 후 재사용하는 HashMap 캐싱 구조를 적용했지만, HashMap은 여러 스레드가 동시에 접근하면 데이터가 꼬일 수 있어 스레드 안전하지 않은 문제가 있음
#### 📌 해결방법과 기대효과
-  ImmutableMap 기반 Eager Init 구조로 전환
-  애플리케이션 시작 시 모든 요청 타입의 설정을 미리 생성해 불변 캐시로 관리하고, 런타임에는 단순 조회만 수행하도록 설계
-  기대효과: 객체 생성 비용이 사라지고 멀티스레드 환경에서도 안정적인 동작을 보장

## ✨ 팀원 역할 분담
#### ✔️ 김민선
- 팀장
- Github 관리
- 도메인 개발: 인증/인가, auth, address, order
#### ✔️ 홍석준
- 테크 리더
- Git 초기화
- 도메인 개발: user+exception, menu
#### ✔️최은서
- 배포
- 도메인 개발: cart, order
#### ✔️ 김부경
- 회의록 작성, 노션 정리
- 도메인 개발: AI, Review
#### ✔️박소정
- Git README 작성
- 도메인 개발: Store, Category
#### ✔️ 유진아
- Menu API 명세서 작성

## ✨ 프로젝트 회고
### 🎯 개발 및 협업 측면에서 우리 조가 잘한 부분
- Rumtime Exception 기반의 공통 예외 처리 정책
- 일관된 응답 반환을 위한 공통 응답 객체 정의
- soft-delete 처리를 위한 삭제 정책
- data 추적/감사를 위한 auditing 정책
### 🎯 현재 우리 시스템의 한계와 이를 발전시키기 위한 계획
- 현재 RefreshToken과 블랙리스트를 DB에 저장하고 만료된 토큰은 스케줄러를 통해 주기적으로 삭제, 이 방식은 서버 재시작 시 스케줄러가 동작하지 않을 수 있음 → Redis를 도입해 토큰 만료시점에 자동으로 삭제되도록 변경하여 성능 향상
### 🎯 협업 시 아쉽거나 부족했던 부분
- 다 함께 코드 리뷰를 하는 시간이 없었던 점이 아쉽
- 프로젝트 초기에 일정계획을 세분화하지 못해서 시간이 부족해 완성도를 높일 시간이 없었던 점이 아쉽
- 도메인 설계 시 파생 도메인과의 연관성과 확장성을 충분히 고려하지 못한 점이 아쉽
- 각자 맡은 부분 구현에 집중해 전체 흐름을 파악할 시간이 없었던 점이 아쉽
- 기능 구현에 집중하느라 공통 문서 관리에 충분히 시간을 할애하지 못한 점이 아쉽
