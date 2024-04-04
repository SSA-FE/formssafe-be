package com.formssafe.domain.content.decoration.entity;

import com.formssafe.global.exception.type.BadRequestException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DecorationType {
    TEXT("text");

    private static final Map<String, DecorationType> convertor =
            Arrays.stream(DecorationType.values())
                    .collect(Collectors.toMap(DecorationType::displayName, Function.identity()));
    private final String displayName;

    DecorationType(String displayName) {
        this.displayName = displayName;
    }

    public static DecorationType from(String type) {
        if (!convertor.containsKey(type)) {
            throw new BadRequestException("유효하지 않은 데코레이션 타입입니다.: " + type);
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
