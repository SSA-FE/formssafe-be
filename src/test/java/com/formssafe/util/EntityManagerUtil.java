package com.formssafe.util;

import jakarta.persistence.EntityManager;

public final class EntityManagerUtil {

    private EntityManagerUtil() {
    }

    public static void flushAndClear(EntityManager em) {
        em.flush();
        em.clear();
    }

    public static void persist(EntityManager em, Object... entities) {
        for (Object entity : entities) {
            em.persist(entity);
        }
    }
}
