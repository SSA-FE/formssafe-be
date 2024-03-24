package com.formssafe.domain.tag.entity;

import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Builder
    private FormTag(Long id, Form form, Tag tag) {
        this.id = id;
        this.form = form;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public Tag getTag() {
        return tag;
    }
}
