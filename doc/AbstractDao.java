package com.bytehonor.sdk.repository.jdbc.dao;

import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.framework.lang.function.ClassGetter;
import com.bytehonor.sdk.framework.lang.function.getter.GetBoolean;
import com.bytehonor.sdk.framework.lang.function.getter.GetInteger;
import com.bytehonor.sdk.framework.lang.function.getter.GetLong;
import com.bytehonor.sdk.framework.lang.function.getter.GetString;
import com.bytehonor.sdk.framework.lang.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.model.GroupStats;
import com.bytehonor.sdk.repository.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.repository.jdbc.model.ModelSetterMapper;

/**
 * TODO to be continue
 * 
 * @param <T>
 */
public abstract class AbstractDao<T> {

    private JdbcProxyDao JdbcProxyDao;

    public final List<T> query(QueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.query(clazz(), condition, setterMapper());
    }

    public final List<T> queryLeftJoin(QueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.queryLeftJoin(clazz(), condition, setterMapper());
    }

    public final T queryById(Long id) {
        Objects.requireNonNull(id, "id");

        return JdbcProxyDao.queryById(clazz(), id, setterMapper());
    }

    public final int delete(QueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.delete(clazz(), condition);
    }

    public final int deleteById(Long id) {
        Objects.requireNonNull(id, "id");

        return JdbcProxyDao.deleteById(clazz(), id);
    }

    public final int count(QueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.count(clazz(), condition);
    }

    public final List<String> strings(GetString<T> getter, QueryCondition condition) {
        return JdbcProxyDao.strings(clazz(), getter, condition);
    }

    public final List<Long> longs(GetLong<T> getter, QueryCondition condition) {
        return JdbcProxyDao.longs(clazz(), getter, condition);
    }

    public final List<Integer> integers(GetInteger<T> getter, QueryCondition condition) {
        return JdbcProxyDao.integers(clazz(), getter, condition);
    }

    public final List<Boolean> booleans(GetBoolean<T> getter, QueryCondition condition) {
        return JdbcProxyDao.booleans(clazz(), getter, condition);
    }

    public final <R> List<R> distinct(ClassGetter<T, R> getter, Class<R> type, QueryCondition condition) {
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.distinct(clazz(), getter, type, condition);
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

    public List<GroupStats> groupCount(ClassGetter<T, ?> getter, QueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        return JdbcProxyDao.groupCount(clazz(), getter, condition);
    }
    
    public abstract Class<T> clazz();

    public abstract ModelSetterMapper<T> setterMapper();

    public abstract ModelGetterMapper<T> getterMapper();
}
