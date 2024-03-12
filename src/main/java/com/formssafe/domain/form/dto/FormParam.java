package com.formssafe.domain.form.dto;

public final class FormParam {

    public record SearchDto(String keyword,
                            String sort,
                            String[] category,
                            String status,
                            String[] tag,
                            Long pageNum) {
    }
}
