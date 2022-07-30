package com.bytehonor.sdk.starter.jdbc.model;

public interface ModelGetterMapper<T> {

    public ModelGetter<T> create(T model);

}
