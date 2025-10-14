package com.delivery.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "서버 에러가 발생했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E002", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E003", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "E004", "접근이 거부되었습니다."),

    // 사용자 도메인
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U002", "이미 가입된 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "U003", "이미 사용 중인 닉네임입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "E005", "아이디 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 토큰입니다."),
    INVALID_NICKNAME(HttpStatus.UNAUTHORIZED, "U004", "아이디(닉네임)가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U004", "비밀번호가 일치하지 않습니다."),
    USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "U007", "접근 권한이 없습니다."),

    // 가게 도메인
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "가게를 찾을 수 없습니다."),
    FORBIDDEN_CREATE_STORE(HttpStatus.FORBIDDEN, "S002", "가게를 생성할 권한이 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "카테고리를 찾을 수 없습니다." ),
    FORBIDDEN_UPDATE_STORE(HttpStatus.FORBIDDEN, "S003", "가게를 수정할 권한이 없습니다."),
    FORBIDDEN_DELETE_STORE(HttpStatus.FORBIDDEN, "S004", "가게를 삭제할 권한이 없습니다."),

    // jwt
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A001", "토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A003", "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "리프레시 토큰이 유효하지 않습니다."),

    // AI 도메인
    AI_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI001", "AI API 호출 중 오류가 발생했습니다."),
    AI_RESPONSE_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "AI002", "AI 응답이 비어있습니다."),
    AI_UNSUPPORTED_REQUEST_TYPE(HttpStatus.BAD_REQUEST, "AI003", "지원하지 않는 요청 타입입니다."),
    AI_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "AI004", "AI 요청 기록을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
