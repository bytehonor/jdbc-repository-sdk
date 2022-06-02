package com.bytehonor.sdk.jdbc.bytehonor.annotation;

@FunctionalInterface
public interface IConvert<F, T> {
    T convert(F form);
}
