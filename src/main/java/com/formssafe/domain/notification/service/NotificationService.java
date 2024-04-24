package com.formssafe.domain.notification.service;

import com.formssafe.domain.notification.dto.NotificationParam.NotificationSearchDto;
import com.formssafe.domain.notification.dto.NotificationResponse.NotificationResponseDto;
import com.formssafe.domain.notification.dto.NotificationResponse.UnreadNotificationCountResponseDto;
import com.formssafe.domain.notification.repository.NotificationRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
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
    private final NotificationRepository notificationRepository;

    public UnreadNotificationCountResponseDto getUnreadNotificationCount(LoginUserDto loginUserDto) {
        int unreadCount = notificationRepository.countByReceiverIdAndIsReadFalse(loginUserDto.id());
        return new UnreadNotificationCountResponseDto(unreadCount);
    }

    public List<NotificationResponseDto> getUnreadNotifications(LoginUserDto loginUserDto) {
        return null;
    }

    public List<NotificationResponseDto> getNotifications(NotificationSearchDto searchDto, LoginUserDto loginUserDto) {
        return null;
    }

    public void markAsRead(Long notificationId, LoginUserDto loginUserDto) {
    }

    public void markAllAsRead(LoginUserDto loginUserDto) {

    }
}
