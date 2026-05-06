package com.bytehonor.sdk.repository.jdbc.sql.rewrite;

/**
 * 列名重写接口。
 *
 * @author lijianqiang
 */
public interface KeyRewriter {

    public String rewrite(String key);
}
