package com.formssafe.domain.notification.service;

import com.formssafe.domain.notification.dto.NotificationParam.NotificationSearchDto;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationListResponseDto;
import com.formssafe.domain.notification.dto.NotificationResponse.UnreadNotificationCountResponseDto;
import com.formssafe.domain.notification.entity.Notification;
import com.formssafe.domain.notification.implement.NotificationMapper;
import com.formssafe.domain.notification.implement.NotificationReader;
import com.formssafe.domain.notification.implement.NotificationUpdater;
import com.formssafe.domain.notification.implement.NotificationValidator;
import com.formssafe.domain.notification.page.PageImpl;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
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
    private final NotificationUpdater notificationUpdater;

    public UnreadNotificationCountResponseDto getUnreadNotificationCount(LoginUserDto loginUserDto) {
        int unreadCount = notificationReader.findUnreadNotificationCount(loginUserDto.id());

        return NotificationMapper.createUnreadNotificationCountResponseDto(unreadCount);
    }

    public NotificationListResponseDto getUnreadNotifications(NotificationSearchDto searchDto,
                                                              LoginUserDto loginUserDto) {
        PageImpl<Notification> unreadNotifications = notificationReader.findUnreadNotifications(loginUserDto.id(),
                searchDto);

        return NotificationMapper.createNotificationListResponseDto(unreadNotifications);
    }

    public NotificationListResponseDto getNotifications(NotificationSearchDto searchDto,
                                                        LoginUserDto loginUserDto) {
        PageImpl<Notification> notifications = notificationReader.findNotifications(loginUserDto.id(), searchDto);

        return NotificationMapper.createNotificationListResponseDto(notifications);
    }

    @Transactional
    public void markAsRead(Long notificationId,
                           LoginUserDto loginUserDto) {
        Notification notification = notificationReader.findNotification(notificationId);
        NotificationValidator.validReceiver(notification, loginUserDto.id());

        notificationUpdater.markAsRead(notification);
    }

    @Transactional
    public void markAllAsRead(LoginUserDto loginUserDto) {
        notificationUpdater.markAllAsRead(loginUserDto.id());
    }
}
