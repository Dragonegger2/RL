package com.sad.function.common;

import java.util.HashMap;

/**
 * Modified HashMap that returns a default value. Use case is the command map structure so that I can
 * always return a null command.
 *
 * @param <K>
 * @param <V>
 */
public class DefaultMap<K, V> extends HashMap<K, V> {
    private V defaultValue;

    public DefaultMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public V get(Object k) {
        return containsKey(k) ? super.get(k) : defaultValue;
    }
}
