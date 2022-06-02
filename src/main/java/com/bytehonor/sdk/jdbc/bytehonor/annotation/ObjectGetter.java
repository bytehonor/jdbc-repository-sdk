package com.bytehonor.sdk.jdbc.bytehonor.annotation;

@FunctionalInterface
public interface ObjectGetter<T> {
    T get();
}
