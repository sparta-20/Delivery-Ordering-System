package com.delivery.ordering.user.controller;

import com.delivery.ordering.ControllerTest;
import com.delivery.ordering.WithMockCustomUser;
import com.delivery.user.controller.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureRestDocs
class UserControllerTest extends ControllerTest {

    @Test
    @DisplayName("내 정보를 조회할 수 있다")
    @WithMockCustomUser(id = 1L, nickname = "hong", email = "hong@example.com", role = "CUSTOMER")
    void 내_정보를_조회할_수_있다() throws Exception {
        mockMvc.perform(
                        get("/api/v1/users/me")
                                .contentType("application/json")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.nickname").value("hong"))
                .andExpect(jsonPath("$.data.email").value("hong@example.com"))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"))
                .andDo(
                        document("get-user-me",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("data").description("데이터").optional(),
                                        fieldWithPath("data.userId").description("사용자 ID"),
                                        fieldWithPath("data.nickname").description("닉네임"),
                                        fieldWithPath("data.email").description("이메일"),
                                        fieldWithPath("data.role").description("사용자 역할"),
                                        fieldWithPath("data.isPublic").description("공개 여부"),
                                        fieldWithPath("data.createdAt").description("생성일"),
                                        fieldWithPath("data.modifiedAt").description("수정일")
                                )
                        )
                );
    }
}
