package com.delivery.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "리뷰 수정 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewUpdateReq {

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 최소 1점입니다.")
    @Max(value = 5, message = "평점은 최대 5점입니다.")
    private int rating;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    @Size(min = 10, max = 500, message = "리뷰 내용은 10자 이상 500자 이하로 입력해야 합니다.")
    private String content;
}