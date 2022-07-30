package com.bytehonor.sdk.starter.jdbc.statement;

import java.util.Objects;

import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;

/**
 * @author lijianqiang
 *
 */
public final class PrepareStatementBuilder {

    public static PrepareStatement select(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new SelectPrepareStatement(clazz, condition);
    }

    public static PrepareStatement selectById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return select(clazz, SqlCondition.id(id));
    }

    public static PrepareStatement count(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new CountPrepareStatement(clazz, condition);
    }

    public static PrepareStatement distinct(Class<?> clazz, String column, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(column, "column");
        Objects.requireNonNull(condition, "condition");

        return new DistinctPrepareStatement(clazz, column, condition);
    }

    public static PrepareStatement insert(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        return new InsertPrepareStatement(clazz);
    }

    public static PrepareStatement update(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new UpdatePrepareStatement(clazz, condition);
    }

    public static PrepareStatement updateById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return new UpdatePrepareStatement(clazz, SqlCondition.id(id));
    }

    public static PrepareStatement delete(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new DeletePrepareStatement(clazz, condition);
    }

    public static PrepareStatement deleteById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return delete(clazz, SqlCondition.id(id));
    }
}
