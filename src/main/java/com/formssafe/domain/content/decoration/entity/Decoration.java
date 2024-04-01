package com.formssafe.domain.content.decoration.entity;

import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Decoration extends Content {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DecorationType type;

    @Builder
    private Decoration(Long id, Form form, String detail, int position, DecorationType type){
        super(id, form, detail, position);
        this.type = type;
    }
}
