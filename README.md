# 🚚AI 검증 비즈니스 프로젝트 

## ✨📣📌🎯👍
## 🎯 프로젝트 개요
- 프로젝트명: '요기요저기요'
- 프로젝트 소개: '요기요저기요'는 음식점들의 배달 주문 관리와 주문 내역 관리 기능을 제공하는 음식 주문 관리 플랫폼입니다.
- 프로젝트 개발 기간: 2025.09.26 ~ 2025.10.20

## 🎯 개발환경 소개
| 분류 | 상세 | 
| --- | --- | 
| IDE | IntelliJ | 
| Language | Java21 | 
| Framework | Spring Boot 3.5.5 | 
| Repository | H2 In-memory, PostgreSQL | 
| Build Tool | Gradle | 
| DevOps - dev | EC2, RDS(PostreSQL), Docker, GihubActions, (Nginx) | 

## 🎯 설계 산출물
### 📌 1. 도메인 다이어그램

### 📌 2. AWS 기반의 운영 환경 다이어그램

### 📌 3. Coding Conventions


## 🎯 개발 산출물
### 🎯 프로젝트 구조

### 🎯 주요 기능
#### 📌 1. 인증/인가 기능
 1. 회원가입: 비밀번호는 BCrypt로 암호화되어 저장
 2. 로그인: 검증에 성공하면 AcessToken과 RefreshToken 발급(AccessToken: 쿠키에 저장, refreshToken: DB에 저장)
 3.  
#### 📌 2. 유저 기능

#### 📌 3. 주소 기능

#### 📌 4. 가게 기능

#### 📌 5. 메뉴 기능

#### 📌 6. 주문 기능

#### 📌 7. AI 기능

#### 📌 8. 리뷰 기능


## 🎯 트러블 슈팅

## 🎯 공통 관심 사항


## 🎯 팀원 역할 분담
#### ✨ 김민선
- 팀장
- Github 관리
- 도메인 개발: 인증/인가, auth, address, order
#### ✨ 홍석준
- 테크 리더
- Git 초기화
- 도메인 개발: user+exception, menu
#### ✨최은서
- 배포
- 도메인 개발: cart, order
#### ✨ 김부경
- 회의록 작성, 노션 정리
- 도메인 개발: AI, Review
#### ✨박소정
- Git README 작성
- 도메인 개발: Store, Category
#### ✨ 유진아
- Menu API 명세서 작성

## 🎯 프로젝트 회고
