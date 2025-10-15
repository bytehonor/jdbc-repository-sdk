package com.bytehonor.sdk.repository.jdbc.function;

@FunctionalInterface
public interface IConvert<F, T> {
    T convert(F form);
}
