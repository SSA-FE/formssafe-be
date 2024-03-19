package com.formssafe.domain.question.entity;

import java.util.Objects;

public class ObjectiveQuestionOption {
    private Integer id;
    private String detail;

    public ObjectiveQuestionOption() {
    }

    public ObjectiveQuestionOption(Integer id, String detail) {
        this.id = id;
        this.detail = detail;
    }

    public Integer getId() {
        return id;
    }

    public String getDetail() {
        return detail;
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

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        return Objects.equals(detail, that.detail);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
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
