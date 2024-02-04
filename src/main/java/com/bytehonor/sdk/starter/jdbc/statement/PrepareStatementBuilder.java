package com.bytehonor.sdk.starter.jdbc.statement;

import java.util.Objects;

import com.bytehonor.sdk.lang.spring.function.ClassGetter;
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

    public static PrepareStatement selectCount(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new SelectCountPrepareStatement(clazz, condition);
    }

    public static <T> PrepareStatement distinct(Class<T> clazz, ClassGetter<T, ?> getter, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(condition, "condition");

        return new DistinctPrepareStatement(clazz, getter, condition);
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

    public static <T> PrepareStatement groupCount(Class<T> clazz, ClassGetter<T, ?> getter, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(condition, "condition");

        return new GroupCountPrepareStatement(clazz, getter, condition);
    }

    public static PrepareStatement leftJoin(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return new LeftJoinPrepareStatement(clazz, condition);
    }

}
