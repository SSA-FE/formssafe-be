package com.formssafe.domain.content.entity;

import com.formssafe.domain.form.entity.Form;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Content {
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

    protected Content(Long id, Form form, String detail, int position){
        this.id = id;
        this.uuid = UUID.randomUUID().toString();
        this.form = form;
        this.detail = detail;
        this.position = position;
    }
}
