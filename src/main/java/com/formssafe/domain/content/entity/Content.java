package com.formssafe.domain.content.entity;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class Content extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true, updatable = false)
    protected String uuid;

    @ManyToOne
    @JoinColumn(name="form_id", nullable = false)
    protected Form form;

    protected String detail;

    protected int position;

    protected Content(Long id, String uuid, Form form, String detail, int position) {
        this.id = id;
        this.uuid = uuid;
        this.form = form;
        this.detail = detail;
        this.position = position;
    }

    protected Content(String uuid, String detail, int position) {
        this.uuid = uuid;
        this.detail = detail;
        this.position = position;
    }
}
