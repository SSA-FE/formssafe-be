package com.formssafe.global.entity;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }
}