package com.bytehonor.sdk.repository.jdbc.model;

/**
 * 模型取值映射接口。
 *
 * @author lijianqiang
 */
public interface ModelGetterMapper<T> {

    public ModelGetter<T> create(T model);

}
