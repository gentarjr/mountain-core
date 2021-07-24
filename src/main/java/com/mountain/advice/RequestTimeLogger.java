package com.mountain.advice;

import com.mountain.library.exceptions.WinterfellException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestTimeLogger {
    @Around("execution(* mountain.controller.*.*.*(..)) || execution(* mountain.controller.*.*.*.*(..))")
    public Object aroundRestMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature method = (MethodSignature) proceedingJoinPoint.getSignature();
        Logger log = LoggerFactory.getLogger(method.getDeclaringType());
        long start = System.currentTimeMillis();

        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            if (t instanceof WinterfellException) {
                log.warn("Exception caught on [{}]: {}", method.getName(), t.getMessage());
            } else {
                log.warn("Exception caught on [{}]: {}", method.getName(), t.getMessage(), t);
            }
            throw t;
        } finally {
            long end = System.currentTimeMillis();
            log.info("Request to {} completed in {} ms", method.getMethod().getName(), end - start);
        }
    }
}
