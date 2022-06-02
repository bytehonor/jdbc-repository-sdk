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

    public List<ModelColumn> out(T t) {
        List<ModelColumn> result = new ArrayList<ModelColumn>();
        for (ModelGetter<T> item : list) {
            Object val = item.value(t);
            result.add(ModelColumn.of(item.getKey(), val, val.getClass().getName()));
        }
        return result;
    }
}
