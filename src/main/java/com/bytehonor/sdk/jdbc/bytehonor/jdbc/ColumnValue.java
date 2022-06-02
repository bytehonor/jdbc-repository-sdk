package com.bytehonor.sdk.jdbc.bytehonor.jdbc;

public class ColumnValue {
    private String key;

    private Object value;

    public ColumnValue() {
        this(null, null);
    }

    public ColumnValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static ColumnValue of(String key, Object value) {
        return new ColumnValue(key, value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
