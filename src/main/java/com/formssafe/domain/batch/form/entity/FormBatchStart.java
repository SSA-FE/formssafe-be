package com.formssafe.domain.batch.form.entity;

import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormBatchStart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime serviceTime;

    @OneToOne
    private Form form;

    @Builder
    private FormBatchStart(Long id, LocalDateTime serviceTime, Form form) {
        this.id = id;
        this.serviceTime = serviceTime;
        this.form = form;
    }
}
