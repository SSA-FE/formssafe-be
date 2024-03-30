package com.formssafe.domain.content.decoration.entity;

import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Decoration extends Content {
    private String type;

    @Builder
    private Decoration(Long id, Form form, String detail, int position, String type){
        super(id, form, detail, position);
        this.type = type;
    }
}
