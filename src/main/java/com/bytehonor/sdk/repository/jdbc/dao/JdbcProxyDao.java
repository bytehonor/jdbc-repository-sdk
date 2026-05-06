package com.bytehonor.sdk.repository.jdbc.dao;

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

import com.bytehonor.sdk.framework.lang.constant.HttpConstants;
import com.bytehonor.sdk.framework.lang.function.ClassGetter;
import com.bytehonor.sdk.framework.lang.function.getter.GetBoolean;
import com.bytehonor.sdk.framework.lang.function.getter.GetInteger;
import com.bytehonor.sdk.framework.lang.function.getter.GetLong;
import com.bytehonor.sdk.framework.lang.function.getter.GetString;
import com.bytehonor.sdk.framework.lang.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.JdbcConfig;
import com.bytehonor.sdk.repository.jdbc.model.GroupStats;
import com.bytehonor.sdk.repository.jdbc.model.ModelField;
import com.bytehonor.sdk.repository.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.repository.jdbc.model.ModelSetterMapper;
import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.repository.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.repository.jdbc.sql.rewrite.PrefixRewriter;
import com.bytehonor.sdk.repository.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.repository.jdbc.statement.PrepareStatementBuilder;
import com.bytehonor.sdk.repository.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.repository.jdbc.util.SqlInjectUtils;

/**
 * JDBC 访问门面，统一封装查询、聚合、增删改能力。
 * <p>
 * 该类负责把 {@link QueryCondition} 转换为内部 {@link SqlCondition}，
 * 再通过不同的 PrepareStatement 生成 SQL 并交给 {@link JdbcTemplate} 执行。
 * </p>
 *
 * @author lijianqiang
 */
public class JdbcProxyDao {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcProxyDao.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcProxyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 按条件查询实体列表。
     *
     * @param clazz 数据模型类型
     * @param condition 查询条件
     * @param mapper 结果映射器
     * @return 命中数据列表
     */
    public <T> List<T> query(Class<T> clazz, QueryCondition condition, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        return doQuery(clazz, SqlConvertor.convert(condition), mapper);
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
        // 单次查询限制，避免一次性全量拉取导致内存峰值过高。
        int limit = HttpConstants.LIMIT_MAX_TOP;
        while (offset < total) {
            // 注意：这里会修改传入的 condition 分页参数，默认用于一次性调用链。
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
        Object[] args = statement.args();

        print(clazz, sql, args);

        return jdbcTemplate.query(sql, args, statement.types(), mapper);
    }

    /**
     * 使用左关联方式查询列表，列名会按 m./s. 前缀改写。
     *
     * @param clazz 左关联视图模型
     * @param condition 查询条件
     * @param mapper 结果映射器
     * @return 命中数据列表
     */
    public <T> List<T> queryLeftJoin(Class<T> clazz, QueryCondition condition, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        return doQueryLeftJoin(clazz, SqlConvertor.convert(condition, PrefixRewriter.leftJoin()), mapper);
    }

    private <T> List<T> doQueryLeftJoin(Class<T> clazz, SqlCondition condition, ModelSetterMapper<T> mapper) {
        PrepareStatement statement = PrepareStatementBuilder.leftJoin(clazz, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        print(clazz, sql, args);

        return jdbcTemplate.query(sql, args, statement.types(), mapper);
    }

    /**
     * 按主键查询单条数据。
     *
     * @param clazz 数据模型类型
     * @param id 主键值
     * @param mapper 结果映射器
     * @return 唯一结果，不存在时返回 null
     */
    public <T> T queryById(Class<T> clazz, Long id, ModelSetterMapper<T> mapper) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        List<T> result = doQuerySelect(clazz, SqlCondition.id(id), mapper);
        return DataAccessUtils.uniqueResult(result);
    }

    /**
     * 按条件删除。
     *
     * @param clazz 数据模型类型
     * @param condition 删除条件
     * @return 受影响行数
     */
    public int delete(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.delete(clazz, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        print(clazz, sql, args);

        return jdbcTemplate.update(sql, args, statement.types());
    }

    /**
     * 按主键删除。
     *
     * @param clazz 数据模型类型
     * @param id 主键值
     * @return 受影响行数
     */
    public int deleteById(Class<?> clazz, Long id) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(id, "id");

        PrepareStatement statement = PrepareStatementBuilder.deleteById(clazz, id);
        String sql = statement.sql();
        Object[] args = statement.args();

        print(clazz, sql, args);

        return jdbcTemplate.update(sql, args, statement.types());
    }

    /**
     * 按条件统计总数。
     *
     * @param clazz 数据模型类型
     * @param condition 统计条件
     * @return 条数
     */
    public int count(Class<?> clazz, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        return doCount(clazz, SqlConvertor.convert(condition));
    }

    private int doCount(Class<?> clazz, SqlCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.selectCount(clazz, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        print(clazz, sql, args);

        return jdbcTemplate.queryForObject(sql, args, statement.types(), Integer.class);
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
     * 查询指定列的去重值集合。
     *
     * @param <T> Model 类类型
     * @param <R> 目标列值类型
     * @param clazz 数据模型类型
     * @param getter 列 getter
     * @param type 列值类型
     * @param condition 查询条件
     * @return 去重后的列值列表
     */
    public <T, R> List<R> distinct(Class<T> clazz, ClassGetter<T, R> getter, Class<R> type, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.distinct(clazz, getter, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        print(clazz, sql, args);

        return jdbcTemplate.queryForList(sql, args, statement.types(), type);
    }

    private void print(Class<?> clazz, String sql, Object[] args) {
        if (JdbcConfig.isInfoEnabled() && LOG.isInfoEnabled()) {
            LOG.info("clazz:{}, sql:[{}], args:[{}]", clazz.getSimpleName(), sql, SqlInjectUtils.toString(args));
        }
    }

    /**
     * 按条件更新。
     *
     * @param model 更新模型
     * @param condition 更新条件
     * @param mapper 字段提取映射器
     * @return 受影响行数
     */
    public <T> int update(T model, QueryCondition condition, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.update(clazz, SqlConvertor.convert(condition));
        statement.prepare(model, mapper);

        String sql = statement.sql();
        Object[] args = statement.args();
        print(clazz, sql, args);

        return jdbcTemplate.update(sql, args, statement.types());
    }

    /**
     * 按主键更新。
     *
     * @param model 更新模型
     * @param id 主键值
     * @param mapper 字段提取映射器
     * @return 受影响行数
     */
    public <T> int updateById(T model, Long id, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.updateById(clazz, id);
        statement.prepare(model, mapper);

        String sql = statement.sql();
        Object[] args = statement.args();
        print(clazz, sql, args);

        return jdbcTemplate.update(sql, args, statement.types());
    }

    /**
     * 插入并返回数据库生成的主键值。
     *
     * @param model 新增模型
     * @param mapper 字段提取映射器
     * @return 生成主键
     */
    public <T> long insert(T model, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        final Class<? extends Object> clazz = model.getClass();
        final PrepareStatement statement = PrepareStatementBuilder.insert(clazz);
        final List<ModelField> fields = statement.prepare(model, mapper);

        final String sql = statement.sql();
        Object[] args = statement.args();
        print(clazz, sql, args);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                // 通过 RETURN_GENERATED_KEYS 让 KeyHolder 可读取自增主键。
                return SqlAdaptUtils.make(sql, fields, connection);
            }
        }, holder);

        return holder.getKey().longValue();
    }

    /**
     * 快速插入（不回填主键）。
     *
     * @param model 新增模型
     * @param mapper 字段提取映射器
     * @return 受影响行数
     */
    public <T> int insertQuick(T model, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        Class<? extends Object> clazz = model.getClass();
        PrepareStatement statement = PrepareStatementBuilder.insert(clazz);
        statement.prepare(model, mapper);

        String sql = statement.sql();
        Object[] args = statement.args();
        print(clazz, sql, args);

        return jdbcTemplate.update(sql, args, statement.types());
    }

    /**
     * 按指定列分组统计。
     *
     * @param clazz 数据模型类型
     * @param getter 分组列 getter
     * @param condition 统计条件
     * @return 分组统计结果
     */
    public <T> List<GroupStats> groupCount(Class<T> clazz, ClassGetter<T, ?> getter, QueryCondition condition) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(condition, "condition");

        PrepareStatement statement = PrepareStatementBuilder.groupCount(clazz, getter, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();
        print(clazz, sql, args);

        return jdbcTemplate.query(sql, args, statement.types(), GroupStats.SETTERS);
    }
}
