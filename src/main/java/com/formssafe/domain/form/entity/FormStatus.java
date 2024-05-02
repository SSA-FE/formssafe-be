package com.formssafe.domain.form.entity;

import com.formssafe.global.error.ErrorCode;
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
    private static final Map<String, FormStatus> convertor = Arrays.stream(FormStatus.values())
            .collect(Collectors.toMap(FormStatus::displayName, Function.identity()));

    FormStatus(String displayName) {
        this.displayName = displayName;
    }

    public static FormStatus from(String type) {
        if (!convertor.containsKey(type)) {
            throw new BadRequestException(ErrorCode.INVALID_FORM_STATUS, "Invalid form status type: " + type);
        }
        return convertor.get(type);
    }

    public String displayName() {
        return displayName;
    }
}
