package com.bytehonor.sdk.starter.jdbc.function;

@FunctionalInterface
public interface ObjectGetter<T> {
    T get();
}
