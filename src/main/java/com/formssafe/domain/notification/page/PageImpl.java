package com.formssafe.domain.notification.page;

import java.util.List;
import lombok.Getter;

@Getter
public class PageImpl<T> {
    private static final int PAGE_SIZE = 10;
    private final List<T> contents;
    private final boolean isLast;
    private final int size;

    public PageImpl(List<T> notifications) {
        this.contents = notifications.subList(0, Math.min(PAGE_SIZE, notifications.size()));
        this.isLast = notifications.size() <= PAGE_SIZE;
        this.size = contents.size();
    }
}
