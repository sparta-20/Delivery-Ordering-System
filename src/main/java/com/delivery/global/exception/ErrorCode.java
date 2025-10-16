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
    INVALID_CONFIRM_NEW_PASSWORD(HttpStatus.BAD_REQUEST, "U008", "변경 비밀번호와 확인 비밀번호가 일치하지 않습니다."),

    // 가게 도메인
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "가게를 찾을 수 없습니다."),
    FORBIDDEN_CREATE_STORE(HttpStatus.FORBIDDEN, "S002", "가게를 생성할 권한이 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "카테고리를 찾을 수 없습니다." ),
    FORBIDDEN_UPDATE_STORE(HttpStatus.FORBIDDEN, "S003", "가게를 수정할 권한이 없습니다."),
    FORBIDDEN_DELETE_STORE(HttpStatus.FORBIDDEN, "S004", "가게를 삭제할 권한이 없습니다."),
    FORBIDDEN_READ_STORE(HttpStatus.FORBIDDEN,"s005", "가게를 조회할 권한이 없습니다." ),
    OUT_OF_SERVICE_AREA(HttpStatus.FORBIDDEN,"S006","서비스 가능 지역 외 요청입니다." ),

    // jwt
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A001", "토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A003", "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "리프레시 토큰이 유효하지 않습니다."),

    // AI 도메인
    AI_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI001", "AI API 호출 중 오류가 발생했습니다."),
    AI_RESPONSE_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "AI002", "AI 응답이 비어있습니다."),
    AI_UNSUPPORTED_REQUEST_TYPE(HttpStatus.BAD_REQUEST, "AI003", "지원하지 않는 요청 타입입니다."),
    AI_NOT_FOUND(HttpStatus.NOT_FOUND, "AI004", "AI 요청 기록을 찾을 수 없습니다."),
    AI_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "AI005", "AI 요청 기록을 삭제할 권한이 없습니다."),
    AI_ACCESS_DENIED(HttpStatus.FORBIDDEN, "AI006", "AI 요청 기록 조회 권한이 없습니다."),
    AI_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI007", "AI 검색 처리 중 오류가 발생했습니다."),

    // 리뷰 도메인
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "R002", "이미 해당 주문에 대한 리뷰가 작성되었습니다."),
    REVIEW_ORDER_NOT_OWNED(HttpStatus.FORBIDDEN, "R003", "본인의 주문에 대해서만 리뷰를 작성할 수 있습니다."),
    REVIEW_READ_FORBIDDEN(HttpStatus.FORBIDDEN, "R004", "리뷰 조회 권한이 없습니다."),
    REVIEW_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "R005", "본인이 작성한 리뷰만 수정할 수 있습니다."),
    REVIEW_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "R006", "리뷰 삭제 권한이 없습니다."),
    REVIEW_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "R007", "리뷰 검색 처리 중 오류가 발생했습니다."),
    REVIEW_ACCESS_DENIED(HttpStatus.FORBIDDEN, "R008", "리뷰 접근 권한이 없습니다."),
    INVALID_REVIEW_RATING(HttpStatus.BAD_REQUEST, "R009", "평점은 1~5점 사이여야 합니다."),
    BAD_REQUEST_STORE_REQUIRED_FOR_CUSTOMER(HttpStatus.BAD_REQUEST, "R010", "고객 리뷰 검색 시 storeId는 필수입니다."),

    // 메뉴 도메인
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "메뉴를 찾을 수 없습니다."),
    MENU_ACCESS_DENIED(HttpStatus.FORBIDDEN, "M002", "해당 메뉴에 접근할 권한이 없습니다."),

    // 주문 도메인
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O001", "주문을 찾을 수 없습니다."),
    ORDER_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "O002", "배송이 완료된 주문만 리뷰 작성이 가능합니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "O003", "주문이 진행중이지 않습니다."),
    ORDER_CANCEL_TIME_EXCEEDED(HttpStatus.BAD_REQUEST, "O004", "주문 취소 가능 시간이 지났습니다."),
    ORDER_ALREADY_COMPLETED(HttpStatus.CONFLICT, "O002", "이미 완료된 주문입니다."),
    ORDER_CREATION_FAILED(HttpStatus.BAD_REQUEST, "O003", "주문 생성에 실패했습니다."),

    // 장바구니 도메인
    DIFFERENT_STORE(HttpStatus.BAD_REQUEST, "C001", "같은 가게의 상품만 장바구니에 담을 수 있습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "장바구니를 찾을 수 없습니다."),
    ITEM_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "I001", "해당 상품을 찾을 수 없습니다."),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "C002", "장바구니에 담긴 상품이 없습니다."),

    // 주소 도메인
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "AD001", "주소를 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}