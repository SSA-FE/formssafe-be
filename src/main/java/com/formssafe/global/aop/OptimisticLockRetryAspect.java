package com.formssafe.global.aop;

import jakarta.persistence.OptimisticLockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.StaleObjectStateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Aspect
@Component
public class OptimisticLockRetryAspect {
    private static final int MAX_RETRIES = 1000;
    private static final int RETRY_DELAY_MS = 100;

    @Pointcut("@annotation(Retry)")
    public void retry() {
    }

    @Around("retry()")
    public Object retryOptimisticLock(ProceedingJoinPoint joinPoint) throws Throwable {
        Exception exceptionHolder = null;
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException | StaleObjectStateException e) {
                exceptionHolder = e;
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
        throw exceptionHolder;
    }
}