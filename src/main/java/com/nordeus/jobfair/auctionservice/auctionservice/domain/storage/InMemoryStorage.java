package com.nordeus.jobfair.auctionservice.auctionservice.domain.storage;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryStorage<K, V extends StorableModel<K>> {
    private final Map<K, V> storage;

    public InMemoryStorage() {
        this.storage = new ConcurrentHashMap<>();
    }

    protected Optional<V> getByKey(K key) {
        return Optional.ofNullable(storage.get(key));
    }

    protected void addValue(V value) {
        storage.put(value.getKey(), value);
    }

    protected Collection<V> getAll() {
        return storage.values();
    }

    protected void delete(K key) {
        storage.remove(key);
    }
}
