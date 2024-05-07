package com.formssafe.util;

import jakarta.persistence.EntityManager;

public final class EntityManagerUtil {

    private EntityManagerUtil() {
    }

    public static void flushAndClearContext(EntityManager entityManager) {
        entityManager.flush();
        entityManager.clear();
    }
}
