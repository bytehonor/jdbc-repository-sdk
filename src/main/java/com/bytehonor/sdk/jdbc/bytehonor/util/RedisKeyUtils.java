package com.bytehonor.sdk.jdbc.bytehonor.util;

import java.util.Objects;

public class RedisKeyUtils {

    private static final String PREFIX = "bytehonor:";

    public static String format(String key) {
        Objects.requireNonNull(key, "key");
        if (key.startsWith(PREFIX) == false) {
            return new StringBuilder(PREFIX).append(key).toString();
        }
        return key;
    }
}
