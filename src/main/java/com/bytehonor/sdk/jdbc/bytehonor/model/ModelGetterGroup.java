package com.bytehonor.sdk.jdbc.bytehonor.model;

import java.util.ArrayList;
import java.util.List;

import com.bytehonor.sdk.jdbc.bytehonor.function.ClassGetter;

public class ModelGetterGroup<T> {

    private List<ModelGetter<T>> list;

    private ModelGetterGroup() {
        this.list = new ArrayList<ModelGetter<T>>();
    }

    public static <T> ModelGetterGroup<T> create(Class<T> clazz) {
        return new ModelGetterGroup<T>();
    }

    public ModelGetterGroup<T> add(String key, ClassGetter<T, ?> getter) {
        list.add(ModelGetter.create(key, getter));
        return this;
    }

    public List<ModelGetter<T>> getList() {
        return list;
    }

    public void setList(List<ModelGetter<T>> list) {
        this.list = list;
    }

    public List<ModelKeyValue> out(T t) {
        List<ModelKeyValue> result = new ArrayList<ModelKeyValue>();
        for (ModelGetter<T> item : list) {
            result.add(item.value(t));
        }
        return result;
    }
}
