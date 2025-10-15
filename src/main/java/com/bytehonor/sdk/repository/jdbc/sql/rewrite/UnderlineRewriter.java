package com.bytehonor.sdk.repository.jdbc.sql.rewrite;

import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;

public class UnderlineRewriter implements KeyRewriter {

    @Override
    public String rewrite(String key) {
        return SqlColumnUtils.camelToUnderline(key);
    }

}
