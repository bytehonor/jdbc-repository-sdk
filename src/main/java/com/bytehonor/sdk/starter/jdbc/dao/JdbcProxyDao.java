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
import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;
import com.bytehonor.sdk.starter.jdbc.model.ModelSetterMapper;
import com.bytehonor.sdk.starter.jdbc.sql.SqlAdapter;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.SqlField;
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

        return query(clazz, SqlAdapter.convert(condition), mapper);
    }

    public <T> List<T> query(Class<T> clazz, SqlCondition condition, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        if (condition.unlimited()) {
            return doQueryAll(clazz, condition, mapper);
        }

        return doQuery(clazz, condition, mapper);
    }

    private <T> List<T> doQueryAll(Class<T> clazz, SqlCondition condition, ModelSetterMapper<T> mapper) {
        int total = count(clazz, condition);
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
            part = doQuery(clazz, condition, mapper);
            if (CollectionUtils.isEmpty(part)) {
                break;
            }
            result.addAll(part);
            offset += limit;
        }
        return result;
    }

    private <T> List<T> doQuery(Class<T> clazz, SqlCondition condition, ModelSetterMapper<T> mapper) {
        PrepareStatement statement = PrepareStatementBuilder.select(clazz, condition);
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.query(sql, statement.args(), statement.types(), mapper);
    }

    public <T> T queryById(Class<T> clazz, Long id, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        List<T> result = query(clazz, SqlCondition.id(id), mapper);
        return DataAccessUtils.uniqueResult(result);
    }

    public int delete(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return delete(clazz, SqlAdapter.convert(condition));
    }

    public int delete(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.delete(clazz, condition);
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

        return count(clazz, SqlAdapter.convert(condition));
    }

    public int count(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.count(clazz, condition);
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.queryForObject(sql, statement.args(), statement.types(), Integer.class);
    }

    public List<String> strings(Class<?> clazz, String column, QueryCondition condition) {
        return distinct(clazz, SqlField.stringer(column), SqlAdapter.convert(condition));
    }

    public List<Long> longs(Class<?> clazz, String column, QueryCondition condition) {
        return distinct(clazz, SqlField.longer(column), SqlAdapter.convert(condition));
    }

    public List<Integer> integers(Class<?> clazz, String column, QueryCondition condition) {
        return distinct(clazz, SqlField.integer(column), SqlAdapter.convert(condition));
    }

    /**
     * @param <T>
     * @param clazz
     * @param field
     * @param condition
     * @return
     */
    public <T> List<T> distinct(Class<?> clazz, SqlField<T> field, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(field, "field");
        Objects.requireNonNull(condition, "condition");

        return distinct(clazz, field, SqlAdapter.convert(condition));
    }

    /**
     * @param <T>
     * @param clazz
     * @param field
     * @param condition
     * @return
     */
    public <T> List<T> distinct(Class<?> clazz, SqlField<T> field, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(field, "field");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.distinct(clazz, field.getColumn(), condition);
        String sql = statement.sql();

        log(clazz, sql);

        return jdbcTemplate.queryForList(sql, statement.args(), statement.types(), field.getType());
    }

    private void log(Class<?> clazz, String sql) {
        if (LOG.isDebugEnabled() == false) {
            return;
        }
        LOG.debug("clazz:{}, sql:{}", clazz.getSimpleName(), sql);
    }

    public <T> int update(T model, QueryCondition condition, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        return update(model, SqlAdapter.convert(condition), mapper);
    }

    public <T> int update(T model, SqlCondition condition, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.update(clazz, condition);
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
}
