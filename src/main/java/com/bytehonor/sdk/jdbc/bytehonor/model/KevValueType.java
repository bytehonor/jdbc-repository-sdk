package com.bytehonor.sdk.jdbc.bytehonor.model;

public class KevValueType {

    private String key;

    private Object value;

    private String type;

    public static KevValueType of(String key, Object value, String type) {
        KevValueType model = new KevValueType();
        model.setKey(key);
        model.setValue(value);
        model.setType(type);
        return model;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
