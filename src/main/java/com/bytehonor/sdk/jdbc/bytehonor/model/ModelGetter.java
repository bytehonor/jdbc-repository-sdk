package com.bytehonor.sdk.jdbc.bytehonor.model;

import com.bytehonor.sdk.jdbc.bytehonor.annotation.Getter;

public class ModelGetter<T> {

    private String key;

    private Getter<T, ?> getter;

    public static <T> ModelGetter<T> create(String key, Getter<T, ?> getter) {
        ModelGetter<T> model = new ModelGetter<T>();
        model.setKey(key);
        model.setGetter(getter);

        return model;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Getter<T, ?> getGetter() {
        return getter;
    }

    public void setGetter(Getter<T, ?> getter) {
        this.getter = getter;
    }

}
