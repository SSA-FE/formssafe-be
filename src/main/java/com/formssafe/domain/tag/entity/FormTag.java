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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormTag {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Getter
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Builder
    private FormTag(Integer id, Form form, Tag tag) {
        this.id = id;
        this.form = form;
        this.tag = tag;
    }
}
