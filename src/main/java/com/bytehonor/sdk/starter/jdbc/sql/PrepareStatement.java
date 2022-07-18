package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.List;

import com.bytehonor.sdk.starter.jdbc.model.ModelColumnValue;
import com.bytehonor.sdk.starter.jdbc.model.ModelConvertMapper;

/**
 * @author lijianqiang
 *
 */
public interface PrepareStatement {

    public <T> List<ModelColumnValue> prepare(T model, ModelConvertMapper<T> mapper);

    public String sql();

    public Object[] args();

    public int[] types();

    public void check();
}
