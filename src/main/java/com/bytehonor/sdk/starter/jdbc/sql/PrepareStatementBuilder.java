package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.Objects;

import com.bytehonor.sdk.starter.jdbc.condition.SqlArgCondition;

public final class PrepareStatementBuilder {

    public static PrepareStatement select(Class<?> clazz, SqlArgCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new SelectPrepareStatement(clazz, condition);
    }

    public static PrepareStatement selectById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return select(clazz, SqlArgCondition.id(id));
    }

    public static PrepareStatement count(Class<?> clazz, SqlArgCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new CountPrepareStatement(clazz, condition);
    }

    public static PrepareStatement insert(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        return new InsertPrepareStatement(clazz);
    }

    public static PrepareStatement update(Class<?> clazz, SqlArgCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new UpdatePrepareStatement(clazz, condition);
    }

    public static PrepareStatement updateById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return new UpdatePrepareStatement(clazz, SqlArgCondition.id(id));
    }

    public static PrepareStatement delete(Class<?> clazz, SqlArgCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new DeletePrepareStatement(clazz, condition);
    }

    public static PrepareStatement deleteById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return delete(clazz, SqlArgCondition.id(id));
    }
}
