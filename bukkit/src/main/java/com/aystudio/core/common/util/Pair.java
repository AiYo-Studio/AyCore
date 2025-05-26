package com.aystudio.core.common.util;

import lombok.Getter;

@Getter
public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K k, V v) {
        this.key = k;
        this.value= v;
    }
}
