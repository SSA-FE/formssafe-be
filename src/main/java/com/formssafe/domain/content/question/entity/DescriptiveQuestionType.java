package com.formssafe.domain.content.question.entity;

import com.formssafe.global.error.type.BadRequestException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DescriptiveQuestionType {
    SHORT("short"),
    LONG("long");

    private static final Map<String, DescriptiveQuestionType> convertor =
            Stream.concat(Arrays.stream(DescriptiveQuestionType.values())
                                    .collect(Collectors.toMap(DescriptiveQuestionType::displayName, Function.identity()))
                                    .entrySet().stream(),
                            Arrays.stream(DescriptiveQuestionType.values())
                                    .collect(Collectors.toMap(DescriptiveQuestionType::name, Function.identity()))
                                    .entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    private final String displayName;

    DescriptiveQuestionType(String displayName) {
        this.displayName = displayName;
    }

    public static DescriptiveQuestionType from(String type) {
        if (!convertor.containsKey(type)) {
            throw new BadRequestException("유효하지 않은 주관식 질문 타입입니다.: " + type);
        }
        return convertor.get(type);
    }

    public static boolean exists(String type) {
        return convertor.containsKey(type);
    }

    public String displayName() {
        return displayName;
    }
}
