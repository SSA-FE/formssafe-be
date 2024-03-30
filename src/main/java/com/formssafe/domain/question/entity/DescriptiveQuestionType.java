package com.formssafe.domain.question.entity;

import com.formssafe.global.exception.type.BadRequestException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DescriptiveQuestionType {
    SHORT("short"),
    LONG("long");

    private final String displayName;
    private static final Map<String, DescriptiveQuestionType> convertor =
            Arrays.stream(DescriptiveQuestionType.values())
                    .collect(Collectors.toMap(DescriptiveQuestionType::displayName, Function.identity()));


    DescriptiveQuestionType(String displayName) {
        this.displayName = displayName;
    }

    public static DescriptiveQuestionType from(String type) {
        if (!convertor.containsKey(type)) {
            throw new BadRequestException("유효하지 않은 주관식 질문 타입입니다.: " + type);
        }
        return convertor.get(type);
    }

    public String displayName() {
        return displayName;
    }
}
