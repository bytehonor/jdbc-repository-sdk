package com.bytehonor.sdk.jdbc.bytehonor.function;

@FunctionalInterface
public interface IConvert<F, T> {
    T convert(F form);
}
