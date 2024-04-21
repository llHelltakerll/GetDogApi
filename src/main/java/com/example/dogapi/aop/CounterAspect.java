package com.example.dogapi.aop;

import com.example.dogapi.util.counter.impl.AtomicCounter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Slf4j
@Configuration
@AllArgsConstructor
public class CounterAspect {

    AtomicCounter atomicCounter;

    @Before("execution(public * com.example.dogapi.controller.*.*(..))")
    public void loggingAdvice() {
        log.info("Counter: {}", atomicCounter.inc());
    }
}
