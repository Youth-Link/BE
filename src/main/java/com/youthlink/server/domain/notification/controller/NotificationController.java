package com.youthlink.server.domain.notification.controller;

import com.youthlink.server.common.apipayload.ApiResponse;
import com.youthlink.server.domain.notification.code.NotificationSuccessCode;
import com.youthlink.server.domain.notification.dto.NotificationResDto;
import com.youthlink.server.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "내 알림 목록 조회", description = "현재 사용자의 알림을 최신순으로 조회합니다")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResDto.NotificationDto>>> getMyNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(NotificationSuccessCode.NOTIFICATION_LIST_OK.getStatus())
                .body(ApiResponse.onSuccess(NotificationSuccessCode.NOTIFICATION_LIST_OK,
                        notificationService.getMyNotifications(userDetails.getUsername())));
    }

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다")
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long notificationId) {
        notificationService.markAsRead(userDetails.getUsername(), notificationId);
        return ResponseEntity.status(NotificationSuccessCode.NOTIFICATION_READ_OK.getStatus())
                .body(ApiResponse.onSuccess(NotificationSuccessCode.NOTIFICATION_READ_OK, null));
    }
}
