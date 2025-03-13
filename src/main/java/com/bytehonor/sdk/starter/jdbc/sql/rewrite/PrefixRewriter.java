package com.bytehonor.sdk.starter.jdbc.sql.rewrite;

import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class PrefixRewriter implements KeyRewriter {

    private final String prefix;

    public PrefixRewriter(String prefix) {
        this.prefix = prefix != null ? prefix : "";
    }

    public static PrefixRewriter of(String prefix) {
        return new PrefixRewriter(prefix);
    }

    public static PrefixRewriter leftJoin() {
        return new PrefixRewriter("m.");
    }

    @Override
    public String rewrite(String key) {
        return prefix + SqlColumnUtils.camelToUnderline(key);
    }

}
