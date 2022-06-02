package com.bytehonor.sdk.jdbc.bytehonor.function;

@FunctionalInterface
public interface ObjectGetter<T> {
    T get();
}
