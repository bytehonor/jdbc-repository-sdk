package com.bytehonor.sdk.jdbc.bytehonor.annotation;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface Getter<T, R> extends Function<T, R>, Serializable {
}
