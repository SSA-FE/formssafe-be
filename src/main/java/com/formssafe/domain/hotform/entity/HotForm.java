package com.formssafe.domain.hotform.entity;

import com.formssafe.domain.form.entity.Form;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HotForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Schema(description = "저장 시간")
    LocalDateTime saveTime;

    @Builder
    private HotForm(Form form, LocalDateTime saveTime) {
        this.form = form;
        this.saveTime = saveTime;
    }
}