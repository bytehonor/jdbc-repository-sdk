package com.bytehonor.sdk.repository.jdbc.model;

public interface ModelGetterMapper<T> {

    public ModelGetter<T> create(T model);

}
