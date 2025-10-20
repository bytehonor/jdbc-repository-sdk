package com.bytehonor.sdk.repository.jdbc.dao;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.bytehonor.sdk.framework.lang.function.ClassGetter;
import com.bytehonor.sdk.framework.lang.function.getter.GetBoolean;
import com.bytehonor.sdk.framework.lang.function.getter.GetInteger;
import com.bytehonor.sdk.framework.lang.function.getter.GetLong;
import com.bytehonor.sdk.framework.lang.function.getter.GetString;
import com.bytehonor.sdk.framework.lang.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.model.GroupCountItem;
import com.bytehonor.sdk.repository.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.repository.jdbc.model.ModelSetterMapper;

public abstract class AbstractDao<T> {

    @Autowired
    private JdbcProxyDao JdbcProxyDao;

    public final List<T> query(Class<T> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.query(clazz, condition, setterMapper());
    }

    public final List<T> queryLeftJoin(Class<T> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.queryLeftJoin(clazz, condition, setterMapper());
    }

    public final T queryById(Class<T> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return JdbcProxyDao.queryById(clazz, id, setterMapper());
    }

    public final int delete(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.delete(clazz, condition);
    }

    public final int deleteById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        return JdbcProxyDao.deleteById(clazz, id);
    }

    public final int count(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.count(clazz, condition);
    }

    public final List<String> strings(Class<T> clazz, GetString<T> getter, QueryCondition condition) {
        return JdbcProxyDao.strings(clazz, getter, condition);
    }

    public final List<Long> longs(Class<T> clazz, GetLong<T> getter, QueryCondition condition) {
        return JdbcProxyDao.longs(clazz, getter, condition);
    }

    public final List<Integer> integers(Class<T> clazz, GetInteger<T> getter, QueryCondition condition) {
        return JdbcProxyDao.integers(clazz, getter, condition);
    }

    public final List<Boolean> booleans(Class<T> clazz, GetBoolean<T> getter, QueryCondition condition) {
        return JdbcProxyDao.booleans(clazz, getter, condition);
    }

    public final <R> List<R> distinct(Class<T> clazz, ClassGetter<T, R> getter, Class<R> type, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.distinct(clazz, getter, type, condition);
    }

    public final int update(T model, QueryCondition condition) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.update(model, condition, getterMapper());
    }

    public final int updateById(T model, Long id) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(id, "id");

        return JdbcProxyDao.updateById(model, id, getterMapper());
    }

    public final long insert(T model) {
        Objects.requireNonNull(model, "model");

        return JdbcProxyDao.insert(model, getterMapper());
    }

    public int insertQuick(T model) {
        Objects.requireNonNull(model, "model");

        return JdbcProxyDao.insertQuick(model, getterMapper());
    }

    public List<GroupCountItem> groupCount(Class<T> clazz, ClassGetter<T, ?> getter, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.groupCount(clazz, getter, condition);
    }

    public abstract ModelSetterMapper<T> setterMapper();

    public abstract ModelGetterMapper<T> getterMapper();
}
