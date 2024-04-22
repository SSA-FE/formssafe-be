package com.formssafe.domain.content.decoration.entity;

import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.UUID;
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

    private Decoration(String uuid, DecorationType type, String detail, int position) {
        super(uuid, detail, position);
        this.type = type;
    }

    @Builder
    private Decoration(Long id, Form form, String detail, int position, DecorationType type){
        super(id, UUID.randomUUID().toString(), form, detail, position);
        this.type = type;
    }

    public static Decoration of(String uuid, DecorationType type, String detail, int position) {
        return new Decoration(uuid, type, detail, position);
    }
}
