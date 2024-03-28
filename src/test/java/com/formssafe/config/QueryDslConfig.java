package com.formssafe.config;

import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private RewardCategoryRepository rewardCategoryRepository;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
