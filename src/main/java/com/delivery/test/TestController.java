package com.delivery.test;

import com.delivery.common.ApiResponse;
import com.delivery.exception.BusinessException;
import com.delivery.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    /**
     * @return {
     * "success": true,
     * "code": "S000",
     * "message": "성공",
     * "data": "value"
     * }
     */
    @GetMapping("/test/v1")
    public ResponseEntity<ApiResponse<String>> get() {

        Long id = testService.save(new TestEntity("value"));
        TestEntity testEntity = testService.getTestEntity(id);

        System.out.println(testEntity.getValue());
        return ResponseEntity.ok(ApiResponse.success(testEntity.getValue()));
    }

    /**
     * @return {
     * "success": false,
     * "code": "U002",
     * "message": "이미 가입된 이메일입니다.",
     * "data": null
     * }
     */
    @GetMapping("/test/error/v1")
    public ResponseEntity<?> getError() {

        throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
    }

    // OWNER 권한만 접근 가능
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/owner-page")
    public String ownerOnly() {
        return "OWNER 권한을 가진 사람만 볼 수 있는 페이지";
    }

    // MASTER 권한만 접근 가능
    @PreAuthorize("hasRole('MASTER')")
    @GetMapping("/master-page")
    public String masterOnly(Authentication authentication) {
        return "MASTER 권한만 볼 수 있는 페이지";
    }

    // CUSTOMER, OWNER, MANAGER, MASTER 모두 접근 가능
//    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER','MASTER')")
    @GetMapping("/all-users")
    public String allUsers() {
        return "모든 로그인 사용자 접근 가능";
    }
}
