package com.delivery.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

@Slf4j
public class PageableUtils {

    private static final Set<Integer> ALLOWED_PAGE_SIZES = Set.of(10, 30, 50);
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * createdAt 기준 Pageable 생성
     * @param page 페이지 번호
     * @param size 페이지 크기 (10, 30, 50만 허용)
     * @param direction 정렬 방향 (ASC/DESC)
     */
    public static Pageable createPageableWithCreatedAt(int page, int size, Sort.Direction direction) {
        int validatedSize = validatePageSize(size);
        log.debug("[PAGEABLE] 생성 완료 - page={}, size={}, sort=createdAt, direction={}",
                page, validatedSize, direction);

        return PageRequest.of(page, validatedSize, Sort.by(direction, "createdAt"));
    }

    // 페이지 크기 검증: 10, 30, 50 이외는 10으로 강제
    private static int validatePageSize(int size) {
        if (!ALLOWED_PAGE_SIZES.contains(size)) {
            log.warn("[PAGEABLE] 허용되지 않은 페이지 크기 - requested={}, allowed={}, applied={}",
                    size, ALLOWED_PAGE_SIZES, DEFAULT_PAGE_SIZE);
            return DEFAULT_PAGE_SIZE;
        }
        return size;
    }
}