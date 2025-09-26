package com.delivery.test;

import com.delivery.common.ApiResponse;
import com.delivery.execption.BusinessException;
import com.delivery.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
