package com.bytehonor.sdk.jdbc.bytehonor.model;

public class ModelKeyValue {

    private String key;

    private Object value;

    private String type;

    public static ModelKeyValue of(String key, Object value, String type) {
        ModelKeyValue model = new ModelKeyValue();
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
