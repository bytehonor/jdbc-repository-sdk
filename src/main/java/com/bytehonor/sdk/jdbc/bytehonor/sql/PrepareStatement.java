package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.List;

import com.bytehonor.sdk.jdbc.bytehonor.model.ModelColumnValue;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelConvertMapper;

public interface PrepareStatement {

    public <T> List<ModelColumnValue> prepare(T model, ModelConvertMapper<T> mapper);

    public String sql();

    public Object[] args();

    public int[] types();
}
