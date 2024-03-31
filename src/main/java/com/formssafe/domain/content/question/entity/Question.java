package com.formssafe.domain.content.question.entity;

import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Question extends Content {
    @Column(nullable = false)
    protected String title;

    protected boolean isRequired;

    protected boolean isPrivacy;

    protected Question(Long id,
                       Form form,
                       String title,
                       String detail,
                       int position,
                       boolean isRequired,
                       boolean isPrivacy) {
        super(id, form, detail, position);
        this.title = title;
        this.isRequired = isRequired;
        this.isPrivacy = isPrivacy;
    }
}