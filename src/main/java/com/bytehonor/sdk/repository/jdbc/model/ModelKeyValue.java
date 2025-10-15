package com.bytehonor.sdk.repository.jdbc.model;

import java.util.Objects;

import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;

public class ModelKeyValue {

    /**
     * 表字段名
     */
    private final String key;

    /**
     * 表字段值
     */
    private final Object value;

    /**
     * 值类型, String Long 等
     */
    private final String javaType;

    public ModelKeyValue(String key, Object value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        this.key = SqlColumnUtils.camelToUnderline(key);
        this.value = value;
        this.javaType = value.getClass().getName();
    }

    public static ModelKeyValue of(String key, Object value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        return new ModelKeyValue(key, value);
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public String getJavaType() {
        return javaType;
    }

}
