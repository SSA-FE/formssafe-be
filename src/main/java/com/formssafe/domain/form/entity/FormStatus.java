package com.formssafe.domain.form.entity;

public enum FormStatus {
    NOT_STARTED("not started"),
    PROGRESS("progress"),
    DONE("done"),
    REWARDED("rewarded");

    private final String displayName;

    FormStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
