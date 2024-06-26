package com.formssafe.domain.form.service;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SortType {
    START_DATE("startDate"),
    END_DATE("endDate");

    private static final Map<String, SortType> convertor
            = Arrays.stream(SortType.values())
            .collect(Collectors.toMap(SortType::displayName, Function.identity()));
    private final String displayName;

    SortType(String displayName) {
        this.displayName = displayName;
    }

    public static SortType from(String displayName) {
        if (displayName == null) {
            return START_DATE;
        }

        return convertor.getOrDefault(displayName, START_DATE);
    }

    public String displayName() {
        return this.displayName;
    }
}
