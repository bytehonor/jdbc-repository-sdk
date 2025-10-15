package com.bytehonor.sdk.repository.jdbc.statement;

import java.util.List;

import com.bytehonor.sdk.repository.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.repository.jdbc.model.ModelKeyValue;

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
