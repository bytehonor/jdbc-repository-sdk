package com.bytehonor.sdk.starter.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.lang.spring.constant.HttpConstants;
import com.bytehonor.sdk.lang.spring.function.ClassGetter;
import com.bytehonor.sdk.lang.spring.function.getter.GetBoolean;
import com.bytehonor.sdk.lang.spring.function.getter.GetInteger;
import com.bytehonor.sdk.lang.spring.function.getter.GetLong;
import com.bytehonor.sdk.lang.spring.function.getter.GetString;
import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.JdbcConfig;
import com.bytehonor.sdk.starter.jdbc.model.GroupCountItem;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;
import com.bytehonor.sdk.starter.jdbc.model.ModelSetterMapper;
import com.bytehonor.sdk.starter.jdbc.sql.SqlAdapter;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.key.PrefixRewriter;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatementBuilder;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;

/**
 * @author lijianqiang
 *
 */
public class JdbcProxyDao {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcProxyDao.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcProxyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> List<T> query(Class<T> clazz, QueryCondition condition, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        return doQuery(clazz, SqlAdapter.convert(condition), mapper);
    }

    private <T> List<T> doQuery(Class<T> clazz, SqlCondition condition, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        if (condition.unlimited()) {
            return doQuerySelectAll(clazz, condition, mapper);
        }

        return doQuerySelect(clazz, condition, mapper);
    }

    private <T> List<T> doQuerySelectAll(Class<T> clazz, SqlCondition condition, ModelSetterMapper<T> mapper) {
        int total = doCount(clazz, condition);
        if (total < 1) {
            return new ArrayList<T>();
        }

        List<T> result = new ArrayList<T>(total * 2);
        List<T> part = new ArrayList<T>();
        int offset = 0;
        int limit = HttpConstants.LIMIT_MAX_TOP;
        while (offset < total) {
            condition.limit(limit);
            condition.offset(offset);
            part = doQuerySelect(clazz, condition, mapper);
            if (CollectionUtils.isEmpty(part)) {
                break;
            }
            result.addAll(part);
            offset += limit;
        }
        return result;
    }

    private <T> List<T> doQuerySelect(Class<T> clazz, SqlCondition condition, ModelSetterMapper<T> mapper) {
        PrepareStatement statement = PrepareStatementBuilder.select(clazz, condition);
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.query(sql, statement.args(), statement.types(), mapper);
    }

    public <T> List<T> queryLeftJoin(Class<T> clazz, QueryCondition condition, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        return doQueryLeftJoin(clazz, SqlAdapter.convert(condition, PrefixRewriter.leftJoin()), mapper);
    }

    private <T> List<T> doQueryLeftJoin(Class<T> clazz, SqlCondition condition, ModelSetterMapper<T> mapper) {
        PrepareStatement statement = PrepareStatementBuilder.leftJoin(clazz, condition);
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.query(sql, statement.args(), statement.types(), mapper);
    }

    public <T> T queryById(Class<T> clazz, Long id, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        List<T> result = doQuery(clazz, SqlCondition.id(id), mapper);
        return DataAccessUtils.uniqueResult(result);
    }

    public int delete(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.delete(clazz, SqlAdapter.convert(condition));
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.update(sql, statement.args(), statement.types());
    }

    public int deleteById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        PrepareStatement statement = PrepareStatementBuilder.deleteById(clazz, id);
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.update(sql, statement.args(), statement.types());
    }

    public int count(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return doCount(clazz, SqlAdapter.convert(condition));
    }

    private int doCount(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.selectCount(clazz, condition);
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.queryForObject(sql, statement.args(), statement.types(), Integer.class);
    }

    public <T> List<String> strings(Class<T> clazz, GetString<T> getter, QueryCondition condition) {
        return distinct(clazz, getter, String.class, condition);
    }

    public <T> List<Long> longs(Class<T> clazz, GetLong<T> getter, QueryCondition condition) {
        return distinct(clazz, getter, Long.class, condition);
    }

    public <T> List<Integer> integers(Class<T> clazz, GetInteger<T> getter, QueryCondition condition) {
        return distinct(clazz, getter, Integer.class, condition);
    }

    public <T> List<Boolean> booleans(Class<T> clazz, GetBoolean<T> getter, QueryCondition condition) {
        return distinct(clazz, getter, Boolean.class, condition);
    }

    /**
     * @param <T>         Model类的类型
     * @param <R>         getter的返回值的类型
     * @param clazz
     * @param getter
     * @param elementType
     * @param condition
     * @return
     */
    public <T, R> List<R> distinct(Class<T> clazz, ClassGetter<T, R> getter, Class<R> elementType,
            QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.distinct(clazz, getter, SqlAdapter.convert(condition));
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.queryForList(sql, statement.args(), statement.types(), elementType);
    }

    private void log(Class<?> clazz, String sql) {
        if (JdbcConfig.isInfoEnabled()) {
            LOG.info("clazz:{}, sql:{}", clazz.getSimpleName(), sql);
        }
    }

    public <T> int update(T model, QueryCondition condition, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.update(clazz, SqlAdapter.convert(condition));
        statement.prepare(model, mapper);

        String sql = statement.sql();
        log(clazz, sql);

        return jdbcTemplate.update(sql, statement.args(), statement.types());
    }

    public <T> int updateById(T model, Long id, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.updateById(clazz, id);
        statement.prepare(model, mapper);

        String sql = statement.sql();
        log(clazz, sql);

        return jdbcTemplate.update(sql, statement.args(), statement.types());
    }

    public <T> long insert(T model, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        final Class<? extends Object> clazz = model.getClass();
        final PrepareStatement statement = PrepareStatementBuilder.insert(clazz);
        final List<ModelKeyValue> items = statement.prepare(model, mapper);

        final String sql = statement.sql();
        log(clazz, sql);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                return SqlAdaptUtils.make(sql, items, connection);
            }
        }, holder);

        return holder.getKey().longValue();
    }

    public <T> int insertQuick(T model, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.insert(clazz);
        statement.prepare(model, mapper);

        String sql = statement.sql();
        log(clazz, sql);

        return jdbcTemplate.update(sql, statement.args(), statement.types());
    }

    public <T> List<GroupCountItem> groupCount(Class<T> clazz, ClassGetter<T, ?> getter, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.groupCount(clazz, getter, SqlAdapter.convert(condition));
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.query(sql, statement.args(), statement.types(), GroupCountItem.SETTERS);
    }
}
