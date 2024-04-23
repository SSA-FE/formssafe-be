package com.formssafe.domain.form.entity;

import com.formssafe.global.error.type.BadRequestException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FormStatus {
    NOT_STARTED("not_started"),
    PROGRESS("progress"),
    DONE("done"),
    REWARDED("rewarded");

    private final String displayName;
    private static final Map<String, FormStatus> convertor =
            Arrays.stream(FormStatus.values())
                    .collect(Collectors.toMap(FormStatus::displayName, Function.identity()));
    FormStatus(String displayName) {
        this.displayName = displayName;
    }
    public static FormStatus from(String type) {
        if (!convertor.containsKey(type)) {
            throw new BadRequestException("유효하지 않은 Form상태 입니다.: " + type);
        }
        return convertor.get(type);
    }

    public String displayName() {
        return displayName;
    }
}
