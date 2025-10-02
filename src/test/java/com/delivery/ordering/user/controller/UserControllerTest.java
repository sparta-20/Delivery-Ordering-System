package com.delivery.ordering.user.controller;

import com.delivery.ordering.TestSecurityConfig;
import com.delivery.ordering.WithMockCustomUser;
import com.delivery.user.controller.UserController;
import com.delivery.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class})
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class UserControllerTest{

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

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
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
    }
}
