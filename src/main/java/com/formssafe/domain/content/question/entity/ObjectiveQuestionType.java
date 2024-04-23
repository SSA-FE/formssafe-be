package com.formssafe.domain.content.question.entity;

import com.formssafe.global.error.type.BadRequestException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ObjectiveQuestionType {
    SINGLE("single"),
    CHECKBOX("checkbox"),
    DROPDOWN("dropdown");

    private static final Map<String, ObjectiveQuestionType> convertor =
            Arrays.stream(ObjectiveQuestionType.values())
                    .collect(Collectors.toMap(ObjectiveQuestionType::displayName, Function.identity()));
    private final String displayName;

    ObjectiveQuestionType(String displayName) {
        this.displayName = displayName;
    }

    public static ObjectiveQuestionType from(String type) {
        if (!convertor.containsKey(type)) {
            throw new BadRequestException("유효하지 않은 객관식 질문 타입입니다.: " + type);
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
