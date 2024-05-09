package com.formssafe.domain.notification.controller;

import com.formssafe.domain.notification.dto.NotificationParam.NotificationSearchDto;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationListResponseDto;
import com.formssafe.domain.notification.dto.NotificationResponse.UnreadNotificationCountResponseDto;
import com.formssafe.domain.notification.service.NotificationService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/notifications")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/unread/count")
    public UnreadNotificationCountResponseDto getUnreadNotificationCount(
            @AuthenticationPrincipal LoginUserDto loginUserDto) {
        return notificationService.getUnreadNotificationCount(loginUserDto);
    }

    @GetMapping("/unread")
    public NotificationListResponseDto getUnreadNotifications(NotificationSearchDto searchDto,
                                                              @AuthenticationPrincipal LoginUserDto loginUserDto) {
        return notificationService.getUnreadNotifications(searchDto, loginUserDto);
    }

    @GetMapping
    public NotificationListResponseDto getNotifications(NotificationSearchDto searchDto,
                                                        @AuthenticationPrincipal LoginUserDto loginUserDto) {
        return notificationService.getNotifications(searchDto, loginUserDto);
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId,
                           @AuthenticationPrincipal LoginUserDto loginUserDto) {
        notificationService.markAsRead(notificationId, loginUserDto);
    }

    @PatchMapping("/read")
    public void markAllAsRead(@AuthenticationPrincipal LoginUserDto loginUserDto) {
        notificationService.markAllAsRead(loginUserDto);
    }
}
