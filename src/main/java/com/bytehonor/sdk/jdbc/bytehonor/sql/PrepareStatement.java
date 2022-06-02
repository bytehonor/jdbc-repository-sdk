package com.bytehonor.sdk.jdbc.bytehonor.sql;

import com.bytehonor.sdk.jdbc.bytehonor.model.ModelMapper;

public interface PrepareStatement {

    public <T> void prepare(T model, ModelMapper<T> mapper);

    public String sql();

    public Object[] args();
}
