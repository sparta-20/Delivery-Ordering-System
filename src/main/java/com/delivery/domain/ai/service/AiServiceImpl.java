package com.delivery.domain.ai.service;

import com.delivery.domain.ai.client.GeminiAiClient;
import com.delivery.domain.ai.config.GeminiProperties;
import com.delivery.domain.ai.dto.AiCreateReq;
import com.delivery.domain.ai.dto.AiRes;
import com.delivery.domain.ai.dto.AiSearchRes;
import com.delivery.domain.ai.entity.Ai;
import com.delivery.domain.ai.entity.RequestTypeEnum;
import com.delivery.domain.ai.repository.AiRepository;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.service.MenuService;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.delivery.global.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiServiceImpl implements AiService {

    private final GeminiProperties geminiProperties;
    private final GeminiAiClient geminiAiClient;
    private final AiRepository aiRepository;
    private final UserService userService;
    private final MenuService menuService;

    // AI 설명 생성 및 기록
    @Override
    @Transactional
    public AiRes createAiContent(Long userId, AiCreateReq request) {
        log.info("[AI] 생성 시작 - userId: {}, menuId: {}, type: {}",
                userId, request.getMenuId(), request.getRequestType());

        // 사용자 / 메뉴 조회
        User user = userService.getUserById(userId);
        Menu menu = menuService.getMenuById(request.getMenuId());

        // 메뉴 접근 권한 검증 (OWNER는 자신의 가게 메뉴만, MANAGER/MASTER는 전체 허용)
        checkWritePermission(user, menu);

        // 프롬프트 가공
        String enhancedPrompt = enhancePrompt(request.getPrompt());

        // Gemini API 호출
        String aiText  = geminiAiClient.generateContent(
                enhancedPrompt,
                request.getRequestType()
        );

        // AI 요청 기록 저장
        Ai savedAi = saveAi(
                user,
                menu,
                request.getRequestType(),
                request.getPrompt(),
                aiText
        );

        log.info("[AI] 생성 완료 - aiId: {}", savedAi.getAiId());
        return AiRes.from(savedAi, userId, menu.getMenuId());
    }

    // AI 요청 기록 단건 조회
    @Override
    public AiRes getAi(Long userId, UserRoleEnum role, UUID aiId) {
        log.info("[AI] 조회 시작 - aiId: {}, userId: {}, role: {}", aiId, userId, role);

        // 미삭제 건만 조회
        Ai ai = getActiveAi(aiId);

        // 조회 권한 검증 (OWNER는 본인이 생성한 것만, MANAGER/MASTER는 전체 허용)
        checkReadPermission(userId, role, ai);

        log.info("[AI] 조회 완료 - aiId: {}", aiId);
        return AiRes.from(ai, userId, ai.getMenu().getMenuId());
    }

    // AI 조회 권한 검증 (OWNER는 본인이 생성한 것만, MANAGER/MASTER는 전체 허용)
    private void checkReadPermission(Long userId, UserRoleEnum role, Ai ai) {
        validateRole(
                role,
                userId,
                ai.getUser().getUserId(),
                ErrorCode.AI_ACCESS_DENIED
        );
    }

    // AI 요청 기록 논리 삭제
    @Override
    @Transactional
    public void softDelete(UUID aiId, Long userId, UserRoleEnum role) {
        log.info("[AI] 삭제 시작 - aiId: {}, userId: {}, role: {}", aiId, userId, role);

        // 미삭제 건만 조회
        Ai ai = getActiveAi(aiId);

        // 삭제 권한 검증 (OWNER는 본인이 생성한 것만, MANAGER/MASTER는 전체 허용)
        checkDeletePermission(userId, role, ai);

        // Soft delete (Timestamped 메서드)
        ai.markDeleted(userId);

        log.info("[AI] 삭제 완료 - aiId: {}, deletedBy: {}", aiId, userId);
    }

    // AI 요청 기록 검색 (페이징)
    @Override
    public Page<AiSearchRes> searchAiRequests(
            RequestTypeEnum requestType,
            Long userId,
            UUID menuId,
            int page,
            int size,
            Sort.Direction direction,
            User currentUser
    ) {
        log.info("[AI_SEARCH] 검색 시작 - requesterId={}, role={}," +
                        " reqType={}, userId={}, menuId={}, page={}, size={}, dir={}",
                currentUser.getUserId(), currentUser.getRole(), requestType, userId, menuId, page, size, direction);

        // Pageable 생성
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);

        // 권한에 따른 userId 결정 (OWNER: 본인 강제, MANAGER/MASTER: 요청 user 또는 전체)
        Long resolvedUserId = determineSearchUserId(userId, currentUser);
        log.debug("[AI_SEARCH] 조회 범위 적용 - resolvedUserId={}",
                resolvedUserId != null ? resolvedUserId : "ALL");

        // 검색 실행 (DB 조회)
        try {
            Page<Ai> aiPage = aiRepository.searchAiRequests(requestType, resolvedUserId, menuId, pageable);

            log.info("[AI_SEARCH] 검색 완료 - total={}, pageNo={}",
                    aiPage.getTotalElements(), aiPage.getNumber());

            return aiPage.map(AiSearchRes::from);

        } catch (DataAccessException dae) {
            log.error("[AI_SEARCH] DB 조회 실패 - reqType={}, resolvedUserId={}, menuId={}, page={}, size={}, dir={}",
                    requestType, resolvedUserId, menuId, page, size, direction, dae);
            throw new BusinessException(ErrorCode.AI_SEARCH_FAILED);
        }
    }

    /**
     * 사용자 역할에 따라 조회 가능한 데이터 범위 제한
     * - OWNER: 본인 데이터만 조회 (요청 파라미터 무시)
     * - MANAGER/MASTER: 전체 또는 특정 사용자 조회  가능
     */
    private Long determineSearchUserId(Long requestedUserId, User currentUser) {
        return (currentUser.getRole() == UserRoleEnum.OWNER)
                ? currentUser.getUserId()
                : requestedUserId;
    }

    // 프롬프트 가공 (요구사항: 50자 이하 안내 문구 첨부)
    private String enhancePrompt(String prompt) {
        return prompt + geminiProperties.getPromptSuffix();
    }

    // AI 요청 기록 저장
    private Ai saveAi(User user, Menu menu, RequestTypeEnum requestType,
                        String prompt, String aiResponse) {

        Ai ai = Ai.builder()
                .user(user)
                .menu(menu)
                .requestType(requestType)
                .prompt(prompt)             // 원문 저장
                .response(aiResponse)
                .build();

        return aiRepository.save(ai);
    }

    // AI 생성 권한 검증 (OWNER는 자신의 가게 메뉴만, MANAGER/MASTER는 전체 허용)
    private void checkWritePermission(User user, Menu menu) {
        validateRole(
                user.getRole(),
                user.getUserId(),
                menu.getStore().getOwner().getUserId(),
                ErrorCode.MENU_ACCESS_DENIED
        );
    }

    // AI 삭제 권한 검증 (OWNER는 본인이 생성한 것만, MANAGER/MASTER는 전체 허용)
    private void checkDeletePermission(Long userId, UserRoleEnum role, Ai ai) {
        validateRole(
                role,
                userId,
                ai.getUser().getUserId(),
                ErrorCode.AI_DELETE_FORBIDDEN
        );
    }

    /**
     * 권한 검증 공통 로직
     * - MASTER, MANAGER: 전체 허용
     * - OWNER: 소유자 일치 여부 확인
     * - 기타: 거부
     *
     * @param role 사용자 권한
     * @param userId 요청한 사용자 ID
     * @param ownerId 리소스 소유자 ID
     * @param errorCode 권한 없을 때 발생시킬 에러 코드
     */
    private void validateRole(UserRoleEnum role, Long userId, Long ownerId, ErrorCode errorCode) {
        switch (role) {
            case MASTER, MANAGER -> {
                // 관리자는 모든 작업 허용
            }
            case OWNER -> {
                // 가게 사장은 본인 소유만 허용
                if (!ownerId.equals(userId)) {
                    log.warn("[AI] 접근 거부 - userId: {}, ownerId: {}, role: {}, errorCode: {}",
                            userId, ownerId, role, errorCode.getCode());
                    throw new BusinessException(errorCode);
                }
            }
            default -> {
                log.warn("[AI] 접근 거부 - userId: {}, role: {}, errorCode: {}",
                        userId, role, errorCode.getCode());
                throw new BusinessException(errorCode);
            }
        }
    }

    // 삭제되지 않은 AI 기록 조회
    private Ai getActiveAi(UUID aiId) {
        return aiRepository.findByAiIdAndDeletedAtIsNull(aiId)
                .orElseThrow(() -> {
                    log.warn("[AI] 기록 없음 또는 이미 삭제됨 - aiId: {}", aiId);
                    return new BusinessException(ErrorCode.AI_NOT_FOUND);
                });
    }
}