package com.example.dogapi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class LRUCache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(LRUCache.class);
    private final int capacity;
    private final LinkedHashMap<K, V> cache;

    @Autowired
    public LRUCache() {
        this(3);
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                boolean shouldRemove = size() > LRUCache.this.capacity;
                if (shouldRemove) {
                    logger.info("Removing entry from cache: {}", LRUCache.this.cache);
                }
                return shouldRemove;
            }
        };
    }

    public void put(K key, V value) {
        cache.put(key, value);
        logger.info("(put)map: {}", LRUCache.this.cache);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void remove(K key) {
        cache.remove(key);
        logger.info("(remove)map: {}", LRUCache.this.cache);
    }

    public void clear() {
        cache.clear();
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public int size() {
        return cache.size();
    }

    public void removeKeysByPrefix(String prefix) {
        Iterator<K> iterator = cache.keySet().iterator();
        while (iterator.hasNext()) {
            K key = iterator.next();
            if (key instanceof String string && string.startsWith(prefix)) {
                iterator.remove();
            }
        }

    }

    public void removeKeysBySuffix(String suffix) {
        Iterator<K> iterator = cache.keySet().iterator();
        while (iterator.hasNext()) {
            K key = iterator.next();
            if (key instanceof String string && string.endsWith(suffix)) {
                iterator.remove();
            }
        }
    }

}
