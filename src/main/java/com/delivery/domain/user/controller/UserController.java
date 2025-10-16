package com.delivery.domain.user.controller;

import com.delivery.domain.user.dto.UpdateUserPasswordReq;
import com.delivery.domain.user.dto.UpdateUserReq;
import com.delivery.domain.user.dto.UserRes;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
@Tag(name = "User", description = "사용자 API")
@SecurityRequirement(name = "JWT")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserRes.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패"
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserRes>> getUserMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User requester = userDetails.getUser();
        UserRes userRes = userService.getUserResById(requester.getUserId());
        return ResponseEntity.ok(ApiRes.success(userRes));
    }

    @Operation(
            summary = "내 정보 수정",
            description = "현재 로그인한 사용자의 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = UserRes.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패"
            )
    })
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserRes>> updateUserMe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateUserReq request)
    {
        User requester = userDetails.getUser();
        UserRes userRes = userService.updateUser(requester.getUserId(), request);
        return ResponseEntity.ok(ApiRes.success(userRes));
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "현재 로그인한 사용자의 비밀번호를 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "변경 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패"
            )
    })
    @PostMapping("/me/password")
    public ResponseEntity<ApiResponse<UserRes>> updateUserMePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateUserPasswordReq request)
    {
        User requester = userDetails.getUser();
        userService.updateUserPassword(requester.getUserId(), request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인한 사용자의 계정을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패"
            )
    })
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<UserRes>> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @CookieValue(value = "accessToken", required = false) String accessToken)
    {
        User requester = userDetails.getUser();
        Long requesterUserId = requester.getUserId();
        Long targetId = requester.getUserId();
        userService.delete(accessToken, requesterUserId, targetId);
        return ResponseEntity.noContent().build();
    }
}
