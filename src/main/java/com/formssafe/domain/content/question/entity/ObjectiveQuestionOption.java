package com.formssafe.domain.content.question.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ObjectiveQuestionOption {
    private int id;
    private String detail;

    public ObjectiveQuestionOption(int id, String detail) {
        this.id = id;
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectiveQuestionOption that = (ObjectiveQuestionOption) o;

        if (id != that.id) {
            return false;
        }
        return Objects.equals(detail, that.detail);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ObjectiveQuestionOption{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                '}';
    }
}
