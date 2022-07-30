package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.List;

import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;

/**
 * @author lijianqiang
 *
 */
public interface PrepareStatement {

    public <T> List<ModelKeyValue> prepare(T model, ModelGetterMapper<T> mapper);

    public String sql();

    public Object[] args();

    public int[] types();

    public void check();
}
