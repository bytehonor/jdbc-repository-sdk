package com.bytehonor.sdk.starter.jdbc.sql.rewrite;

import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class UnderlineRewriter implements KeyRewriter {

    @Override
    public String rewrite(String key) {
        return SqlColumnUtils.camelToUnderline(key);
    }

}
