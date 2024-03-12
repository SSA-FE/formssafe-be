package com.formssafe.domain.tag.dto;

public final class TagResponse {

    public record CountDto(Long id,
                           String name,
                           int count) {
    }

    public record ListDto(Long id,
                          String name) {
    }
}


