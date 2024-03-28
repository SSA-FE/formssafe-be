package com.formssafe.domain.form.entity;

import java.util.Map;

public enum FormStatus {
    NOT_STARTED("not_started"),
    PROGRESS("progress"),
    DONE("done"),
    REWARDED("rewarded");

    private final String displayName;
    public static FormStatus convertor(String status){
        return FormStatus.valueOf(status.toUpperCase());
    }

    FormStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
