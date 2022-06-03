package com.bytehonor.sdk.jdbc.bytehonor.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.bytehonor.sdk.jdbc.bytehonor.model.ModelColumnValue;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelConvertMapper;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
import com.bytehonor.sdk.jdbc.bytehonor.sql.PrepareStatement;
import com.bytehonor.sdk.jdbc.bytehonor.sql.PrepareStatementBuilder;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlAdaptUtils;

public class JdbcProxyDao {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcProxyDao.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcProxyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> List<T> query(Class<?> clazz, QueryCondition condition, RowMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        PrepareStatement statement = PrepareStatementBuilder.select(clazz, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        log(clazz, sql, args);

        return jdbcTemplate.query(sql, args, statement.types(), mapper);
    }

    public <T> T queryById(Class<T> clazz, Long id, RowMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        List<T> result = query(clazz, QueryCondition.id(id), mapper);
        return DataAccessUtils.uniqueResult(result);
    }

    public int delete(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.delete(clazz, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        log(clazz, sql, args);

        return jdbcTemplate.update(statement.sql(), statement.args(), statement.types());
    }

    public int deleteById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        PrepareStatement statement = PrepareStatementBuilder.deleteById(clazz, id);
        String sql = statement.sql();
        Object[] args = statement.args();

        log(clazz, sql, args);

        return jdbcTemplate.update(statement.sql(), statement.args(), statement.types());
    }

    public int count(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.count(clazz, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        log(clazz, sql, args);

        return jdbcTemplate.queryForObject(sql, args, statement.types(), Integer.class);
    }

    private void log(Class<?> clazz, String sql, Object[] args) {
        if (LOG.isDebugEnabled() == false) {
            return;
        }
        LOG.debug("clazz:{}, sql:{}", clazz.getSimpleName(), sql);
        for (Object arg : args) {
            LOG.debug("arg:{}", arg);
        }
    }

    public <T> int update(T model, QueryCondition condition, ModelConvertMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.update(clazz, condition);
        statement.prepare(model, mapper);

        String sql = statement.sql();
        Object[] args = statement.args();
        log(clazz, sql, args);

        return jdbcTemplate.update(sql, args);
    }

    public <T> long insert(T model, ModelConvertMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        final Class<? extends Object> clazz = model.getClass();
        final PrepareStatement statement = PrepareStatementBuilder.insert(clazz);
        final List<ModelColumnValue> items = statement.prepare(model, mapper);

        String sql = statement.sql();
        Object[] args = statement.args();
        log(clazz, sql, args);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                return SqlAdaptUtils.convert(statement, items, connection);
            }
        }, holder);

        return holder.getKey().longValue();
    }

    public <T> int insertQuick(T model, ModelConvertMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.insert(clazz);
        statement.prepare(model, mapper);

        String sql = statement.sql();
        Object[] args = statement.args();
        log(clazz, sql, args);

        return jdbcTemplate.update(sql, args);
    }
}
