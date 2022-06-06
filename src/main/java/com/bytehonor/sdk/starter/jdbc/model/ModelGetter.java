package com.bytehonor.sdk.starter.jdbc.model;

import java.util.Objects;

import com.bytehonor.sdk.starter.jdbc.function.ClassGetter;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class ModelGetter<T> {

    private String key;

    private ClassGetter<T, ?> getter;

    public static <T> ModelGetter<T> create(String key, ClassGetter<T, ?> getter) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(getter, "getter");

        ModelGetter<T> model = new ModelGetter<T>();
        model.setKey(key);
        model.setGetter(getter);

        return model;
    }

    public ModelColumnValue value(T t) {
        Objects.requireNonNull(t, "t");

        Object val = this.getter.apply(t);
        if (val == null) {
            return null;
        }
        return ModelColumnValue.of(SqlColumnUtils.camelToUnderline(key), val, val.getClass().getName());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ClassGetter<T, ?> getGetter() {
        return getter;
    }

    public void setGetter(ClassGetter<T, ?> getter) {
        this.getter = getter;
    }

}
