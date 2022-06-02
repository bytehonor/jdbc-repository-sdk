package com.bytehonor.sdk.jdbc.bytehonor.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.annotation.Getter;

public class ModelGetterGroup<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ModelGetterGroup.class);

    private List<ModelGetter<T>> list;

    private ModelGetterGroup() {

    }

    public static <T> ModelGetterGroup<T> create(Class<T> clazz) {
        ModelGetterGroup<T> model = new ModelGetterGroup<T>();
        model.setList(new ArrayList<ModelGetter<T>>());
        return model;
    }

    public ModelGetterGroup<T> add(String key, Getter<T, ?> getter) {
        list.add(ModelGetter.create(key, getter));
        return this;
    }

    public List<ModelGetter<T>> getList() {
        return list;
    }

    public void setList(List<ModelGetter<T>> list) {
        this.list = list;
    }

    public void print(T t) {
        for (ModelGetter<T> item : list) {
            Object obj = item.getGetter().apply(t);
            LOG.info("key:{}, obj:{}, class:{}", item.getKey(), obj, obj.getClass().getSimpleName());
        }
    }
}
