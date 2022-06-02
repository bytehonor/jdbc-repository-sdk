package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.List;

import com.bytehonor.sdk.jdbc.bytehonor.model.ModelKeyValue;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelMapper;

public interface PrepareStatement {

    public <T> List<ModelKeyValue> prepare(T model, ModelMapper<T> mapper);

    public String sql();

    public Object[] args();

    public int[] types();
}
