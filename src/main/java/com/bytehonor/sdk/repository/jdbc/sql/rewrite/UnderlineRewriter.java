package com.bytehonor.sdk.repository.jdbc.sql.rewrite;

import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;

/**
 * 默认列名重写实现：驼峰转下划线。
 *
 * @author lijianqiang
 */
public class UnderlineRewriter implements KeyRewriter {

    @Override
    public String rewrite(String key) {
        return SqlColumnUtils.camelToUnderline(key);
    }

}
