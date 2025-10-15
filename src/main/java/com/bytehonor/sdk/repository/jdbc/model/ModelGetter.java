package com.bytehonor.sdk.repository.jdbc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.lang.spring.function.Getters;
import com.bytehonor.sdk.lang.spring.function.getter.GetBoolean;
import com.bytehonor.sdk.lang.spring.function.getter.GetDouble;
import com.bytehonor.sdk.lang.spring.function.getter.GetInteger;
import com.bytehonor.sdk.lang.spring.function.getter.GetLong;
import com.bytehonor.sdk.lang.spring.function.getter.GetString;

public class ModelGetter<T> {

    private final T model;

    private final List<ModelKeyValue> keyValues;

    public ModelGetter(T model) {
        Objects.requireNonNull(model, "model");

        this.model = model;
        this.keyValues = new ArrayList<ModelKeyValue>();
    }

    public static <T> ModelGetter<T> of(T model) {
        Objects.requireNonNull(model, "model");

        return new ModelGetter<T>(model);
    }

    public void add(GetString<T> getter) {
        String value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        keyValues.add(ModelKeyValue.of(key, value));
    }

    public void add(GetLong<T> getter) {
        Long value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        keyValues.add(ModelKeyValue.of(key, value));
    }

    public void add(GetInteger<T> getter) {
        Integer value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        keyValues.add(ModelKeyValue.of(key, value));
    }

    public void add(GetBoolean<T> getter) {
        Boolean value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        keyValues.add(ModelKeyValue.of(key, value));
    }

    public void add(GetDouble<T> getter) {
        Double value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        keyValues.add(ModelKeyValue.of(key, value));
    }

    public T getModel() {
        return model;
    }

    public List<ModelKeyValue> getKeyValues() {
        return keyValues;
    }

}
