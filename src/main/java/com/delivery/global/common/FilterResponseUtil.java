package com.delivery.global.common;

import com.delivery.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FilterResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ApiResponse<?> apiResponse = ApiResponse.error(errorCode.getCode(), errorCode.getMessage());
        sendResponse(response, errorCode.getStatus().value(), apiResponse);
    }

    public static void sendSuccess(HttpServletResponse response, Object data) throws IOException {
        ApiResponse<?> apiResponse = ApiResponse.success(data);
        sendResponse(response, HttpServletResponse.SC_OK, apiResponse);
    }

    private static void sendResponse(HttpServletResponse response, int status, ApiResponse<?> body) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), body);
    }
}
