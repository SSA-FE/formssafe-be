package com.formssafe.domain.notification.service;

import com.formssafe.domain.notification.dto.NotificationParam.NotificationSearchDto;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationResponseDto;
import com.formssafe.domain.notification.dto.NotificationResponse.UnreadNotificationCountResponseDto;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.implement.NotificationReader;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationReader notificationReader;

    public UnreadNotificationCountResponseDto getUnreadNotificationCount(LoginUserDto loginUserDto) {
        int unreadCount = notificationReader.getUnreadNotificationCount(loginUserDto.id());
        return new UnreadNotificationCountResponseDto(unreadCount);
    }

    public List<NotificationResponseDto> getUnreadNotifications(LoginUserDto loginUserDto) {
        List<Notification> notifications = notificationReader.getUnreadNotifications(loginUserDto.id());

        return notifications.stream()
                .map(NotificationResponseDto::from)
                .sorted((n1, n2) -> n2.createDate().compareTo(n1.createDate()))
                .toList();
    }

    public List<NotificationResponseDto> getNotifications(NotificationSearchDto searchDto, LoginUserDto loginUserDto) {
        return Collections.EMPTY_LIST;
    }

    public void markAsRead(Long notificationId, LoginUserDto loginUserDto) {
    }

    public void markAllAsRead(LoginUserDto loginUserDto) {

    }
}
