package com.bytehonor.sdk.repository.jdbc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.framework.lang.function.Getters;
import com.bytehonor.sdk.framework.lang.function.getter.GetBoolean;
import com.bytehonor.sdk.framework.lang.function.getter.GetDouble;
import com.bytehonor.sdk.framework.lang.function.getter.GetInteger;
import com.bytehonor.sdk.framework.lang.function.getter.GetLong;
import com.bytehonor.sdk.framework.lang.function.getter.GetString;

public class ModelGetter<T> {

    private final T model;

    private final List<ModelField> fields;

    public ModelGetter(T model) {
        Objects.requireNonNull(model, "model");

        this.model = model;
        this.fields = new ArrayList<ModelField>();
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
        fields.add(ModelField.of(key, value));
    }

    public void add(GetLong<T> getter) {
        Long value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        fields.add(ModelField.of(key, value));
    }

    public void add(GetInteger<T> getter) {
        Integer value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        fields.add(ModelField.of(key, value));
    }

    public void add(GetBoolean<T> getter) {
        Boolean value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        fields.add(ModelField.of(key, value));
    }

    public void add(GetDouble<T> getter) {
        Double value = Getters.get(getter, model);
        if (value == null) {
            return;
        }
        String key = Getters.field(getter);
        fields.add(ModelField.of(key, value));
    }

    public T getModel() {
        return model;
    }

    public List<ModelField> getFields() {
        return fields;
    }

}
