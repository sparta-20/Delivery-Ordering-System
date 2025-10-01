package com.delivery.ordering;

/**
 * 공통 응답 포맷이 변경될 경우를 대비
 */
public enum RestDocsPrefix {
    DATA("data");

    private final String content;

    RestDocsPrefix(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}