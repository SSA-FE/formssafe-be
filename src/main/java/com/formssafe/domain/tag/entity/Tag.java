package com.formssafe.domain.tag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Tag {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Column(nullable = false)
    private String tagName;

    @Getter
    private int count;

    @Getter
    @OneToMany(mappedBy = "tag")
    private List<FormTag> formTagList = new ArrayList<>();

    @Builder
    private Tag(Integer id, String tagName, int count) {
        this.id = id;
        this.tagName = tagName;
        this.count = count;
    }
}
