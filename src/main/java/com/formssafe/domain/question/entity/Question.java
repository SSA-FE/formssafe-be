package com.formssafe.domain.question.entity;

import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

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
        this.form = form;
        this.title = title;
        this.detail = detail;
        this.position = position;
        this.isRequired = isRequired;
        this.isPrivacy = isPrivacy;
    }

    public Long getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getPosition() {
        return position;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public boolean isPrivacy() {
        return isPrivacy;
    }
}