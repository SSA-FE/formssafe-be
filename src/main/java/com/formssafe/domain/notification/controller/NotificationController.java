package com.formssafe.domain.notification.controller;

import com.formssafe.domain.notification.dto.NotificationParam.NotificationSearchDto;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationResponseDto;
import com.formssafe.domain.notification.dto.NotificationResponse.UnreadNotificationCountResponseDto;
import com.formssafe.domain.notification.service.NotificationService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        log.info(loginUserDto.toString());
        return notificationService.getUnreadNotificationCount(loginUserDto);
    }

    @GetMapping("/unread")
    public List<NotificationResponseDto> getUnreadNotifications(@AuthenticationPrincipal LoginUserDto loginUserDto) {
        log.info(loginUserDto.toString());
        return notificationService.getUnreadNotifications(loginUserDto);
    }

    @GetMapping
    public List<NotificationResponseDto> getNotifications(@ModelAttribute NotificationSearchDto searchDto,
                                                          @AuthenticationPrincipal LoginUserDto loginUserDto) {
        log.info(searchDto.toString());
        log.info(loginUserDto.toString());
        return notificationService.getNotifications(searchDto, loginUserDto);
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId,
                           @AuthenticationPrincipal LoginUserDto loginUserDto) {
        log.info(notificationId.toString());
        log.info(loginUserDto.toString());
        notificationService.markAsRead(notificationId, loginUserDto);
    }

    @PatchMapping("/read")
    public void markAllAsRead(@AuthenticationPrincipal LoginUserDto loginUserDto) {
        log.info(loginUserDto.toString());
        notificationService.markAllAsRead(loginUserDto);
    }
}
