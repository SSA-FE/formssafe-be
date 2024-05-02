package com.formssafe.domain.content.decoration.entity;

import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DecorationType {
    TEXT("text");

    private static final Map<String, DecorationType> convertor =
            Stream.concat(
                            Arrays.stream(DecorationType.values())
                                    .collect(Collectors.toMap(DecorationType::displayName, Function.identity()))
                                    .entrySet().stream(),
                            Arrays.stream(DecorationType.values())
                                    .collect(Collectors.toMap(DecorationType::name, Function.identity()))
                                    .entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    private final String displayName;

    DecorationType(String displayName) {
        this.displayName = displayName;
    }

    public static DecorationType from(String type) {
        if (!convertor.containsKey(type)) {
            throw new BadRequestException(ErrorCode.SYSTEM_ERROR, "유효하지 않은 데코레이션 타입입니다.: " + type);
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
