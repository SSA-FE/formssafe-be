package com.formssafe.domain.question.entity;

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
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true, updatable = false)
    protected String uuid;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    protected Form form;

    @Column(nullable = false)
    protected String title;

    protected String detail;

    protected int position;

    protected boolean isRequired;

    protected boolean isPrivacy;

    protected Question(Long id,
                       Form form,
                       String title,
                       String detail,
                       int position,
                       boolean isRequired,
                       boolean isPrivacy) {
        this.id = id;
        this.uuid = UUID.randomUUID().toString();
        this.form = form;
        this.title = title;
        this.detail = detail;
        this.position = position;
        this.isRequired = isRequired;
        this.isPrivacy = isPrivacy;
    }
}