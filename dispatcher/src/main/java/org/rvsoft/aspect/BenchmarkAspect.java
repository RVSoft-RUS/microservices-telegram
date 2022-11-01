package org.rvsoft.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class BenchmarkAspect {

    @Around("@annotation(org.rvsoft.aspect.annotation.Benchmark)")
    public Object aroundService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return benchmarkMethod(proceedingJoinPoint);
    }

    private Object benchmarkMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            return proceedingJoinPoint.proceed();
        } finally {
            System.out.println("Время выполнения метода " +
                    proceedingJoinPoint.toShortString() + ": " + (System.currentTimeMillis() - start));
        }
    }
}
