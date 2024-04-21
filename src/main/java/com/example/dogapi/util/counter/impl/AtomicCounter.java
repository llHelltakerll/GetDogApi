package com.example.dogapi.util.counter.impl;

import com.example.dogapi.util.counter.Counter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AtomicCounter implements Counter {
    AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public int get() {
        return atomicInteger.get();
    }

    @Override
    public int inc() {
        return atomicInteger.incrementAndGet();
    }
}
