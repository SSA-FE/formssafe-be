package com.formssafe.domain.notification.entity;

import com.formssafe.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User receiver;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    @Column(nullable = false)
    private String content;

    private boolean isRead;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @Builder
    private Notification(Long id,
                         User receiver,
                         NotificationType notificationType,
                         String content,
                         boolean isRead) {
        this.id = id;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = content;
        this.isRead = isRead;
    }

    public void read() {
        this.isRead = true;
    }
}
