package com.bytehonor.sdk.starter.jdbc.function;

@FunctionalInterface
public interface IConvert<F, T> {
    T convert(F form);
}
