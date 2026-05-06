package com.bytehonor.sdk.repository.jdbc.sql.rewrite;

import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;

/**
 * 带前缀的列名重写实现（如 left join 场景的 m./s.）。
 *
 * @author lijianqiang
 */
public class PrefixRewriter implements KeyRewriter {

    private final String prefix;

    public PrefixRewriter(String prefix) {
        this.prefix = prefix != null ? prefix : "";
    }

    public static PrefixRewriter of(String prefix) {
        return new PrefixRewriter(prefix);
    }

    public static PrefixRewriter leftJoin() {
        // left join 默认主表别名前缀为 m.
        return new PrefixRewriter("m.");
    }

    @Override
    public String rewrite(String key) {
        return prefix + SqlColumnUtils.camelToUnderline(key);
    }

}
