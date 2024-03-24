package com.formssafe.domain.tag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tagName;

    private int count;

    @OneToMany(mappedBy = "tag")
    private List<FormTag> formTagList = new ArrayList<>();

    @Builder
    private Tag(Long id, String tagName, int count) {
        this.id = id;
        this.tagName = tagName;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public int getCount() {
        return count;
    }

    public List<FormTag> getFormTagList() {
        return formTagList;
    }
}
