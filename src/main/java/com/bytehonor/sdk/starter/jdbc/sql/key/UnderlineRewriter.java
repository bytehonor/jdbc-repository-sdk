package com.bytehonor.sdk.starter.jdbc.sql.key;

import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class UnderlineRewriter implements KeyRewriter {

    @Override
    public String rewrite(String key) {
        return SqlColumnUtils.camelToUnderline(key);
    }

}
