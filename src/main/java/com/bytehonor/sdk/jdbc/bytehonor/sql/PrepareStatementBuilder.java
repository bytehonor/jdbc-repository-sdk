package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.Objects;

import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;

public final class PrepareStatementBuilder {

    public static PrepareStatement select(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new SelectPrepareStatement(clazz, condition);
    }

    public static PrepareStatement selectById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return select(clazz, QueryCondition.id(id));
    }

    public static PrepareStatement count(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new CountPrepareStatement(clazz, condition);
    }

    public static PrepareStatement insert(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        return new InsertPrepareStatement(clazz);
    }

    public static PrepareStatement update(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new UpdatePrepareStatement(clazz, condition);
    }

    public static PrepareStatement delete(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new DeletePrepareStatement(clazz, condition);
    }

    public static PrepareStatement deleteById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return delete(clazz, QueryCondition.id(id));
    }
}
