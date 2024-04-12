package com.example.dogapi.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Aspect
@Slf4j
@Configuration
public class LoggingAspectConfig {

    @Around("execution(public * com.example.dogapi.controller.*.*(..))")
    public Object loggingAdvice(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] args = pjp.getArgs();
        log.info("method invoked " + className + " : " + methodName + "()"
                + " arguments : " + Arrays.toString(args));
        Object object = pjp.proceed();
        log.info(className + " : " + methodName + "()"
                + " response: " + mapper.writeValueAsString(object));
        return object;
    }
}
