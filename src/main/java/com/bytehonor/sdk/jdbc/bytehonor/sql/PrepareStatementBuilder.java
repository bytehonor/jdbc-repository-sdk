package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.Objects;

import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaParseUtils;
import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchCondition;

public final class PrepareStatementBuilder {

    private PrepareStatementBuilder() {

    }

    public static PrepareStatement select(Class<?> clazz, MatchCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        MetaTable table = MetaParseUtils.parse(clazz);
        return new SelectPrepareStatement(table, condition);
    }

    public static PrepareStatement selectById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return select(clazz, MatchCondition.id(id));
    }

    public static PrepareStatement count(Class<?> clazz, MatchCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        MetaTable table = MetaParseUtils.parse(clazz);
        return new CountPrepareStatement(table, condition);
    }

    public static PrepareStatement update(Class<?> clazz, MatchCondition condition) {
        MetaTable table = MetaParseUtils.parse(clazz);
        return new SelectPrepareStatement(table, condition);
    }

    public static PrepareStatement delete(Class<?> clazz, MatchCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        MetaTable table = MetaParseUtils.parse(clazz);
        return new DeletePrepareStatement(table, condition);
    }

    public static PrepareStatement deleteById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return delete(clazz, MatchCondition.id(id));
    }
}
