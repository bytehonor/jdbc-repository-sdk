package com.bytehonor.sdk.starter.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.define.spring.constant.JavaValueTypes;
import com.bytehonor.sdk.lang.spring.getter.BooleanGetter;
import com.bytehonor.sdk.lang.spring.getter.DoubleGetter;
import com.bytehonor.sdk.lang.spring.getter.IntegerGetter;
import com.bytehonor.sdk.lang.spring.getter.LongGetter;
import com.bytehonor.sdk.lang.spring.util.JoinUtils;
import com.bytehonor.sdk.starter.jdbc.constant.SqlValueTypes;
import com.bytehonor.sdk.starter.jdbc.model.ModelColumnValue;

public class SqlAdaptUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlAdaptUtils.class);

    public static PreparedStatement make(String sql, List<ModelColumnValue> items, Connection connection)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        for (ModelColumnValue item : items) {
            set(ps, idx, item);
            idx++;
        }
        return ps;
    }

    private static void set(PreparedStatement ps, int idx, ModelColumnValue item) throws SQLException {
        if (JavaValueTypes.STRING.equals(item.getType())) {
            ps.setString(idx, item.getValue().toString());
            return;
        }
        if (JavaValueTypes.LONG.equals(item.getType())) {
            ps.setLong(idx, LongGetter.optional(item.getValue().toString(), 0L));
            return;
        }
        if (JavaValueTypes.INTEGER.equals(item.getType())) {
            ps.setInt(idx, IntegerGetter.optional(item.getValue().toString(), 0));
            return;
        }
        if (JavaValueTypes.BOOLEAN.equals(item.getType())) {
            ps.setBoolean(idx, BooleanGetter.optional(item.getValue().toString(), false));
            return;
        }
        if (JavaValueTypes.DOUBLE.equals(item.getType())) {
            ps.setDouble(idx, DoubleGetter.optional(item.getValue().toString(), 0.0));
            return;
        }
        LOG.error("not support type, set key:{}, value:{}, type:{}", item.getColumn(), item.getValue(), item.getType());
        throw new RuntimeException("not support type");
    }

    public static int toSqlType(String type) {
        if (JavaValueTypes.STRING.equals(type)) {
            return SqlValueTypes.STRING;
        }
        if (JavaValueTypes.LONG.equals(type)) {
            return SqlValueTypes.LONG;
        }
        if (JavaValueTypes.INTEGER.equals(type)) {
            return SqlValueTypes.INTEGER;
        }
        if (JavaValueTypes.BOOLEAN.equals(type)) {
            return SqlValueTypes.BOOLEAN;
        }
        if (JavaValueTypes.DOUBLE.equals(type)) {
            return SqlValueTypes.DOUBLE;
        }
        LOG.error("not support type, type:{}", type);
        throw new RuntimeException("not support type");
    }

    public static String toJavaType(int type) {
        if (SqlValueTypes.STRING == type) {
            return JavaValueTypes.STRING;
        }
        if (SqlValueTypes.LONG == type) {
            return JavaValueTypes.LONG;
        }
        if (SqlValueTypes.INTEGER == type) {
            return JavaValueTypes.INTEGER;
        }
        if (SqlValueTypes.BOOLEAN == type) {
            return JavaValueTypes.BOOLEAN;
        }
        if (SqlValueTypes.DOUBLE == type) {
            return JavaValueTypes.DOUBLE;
        }
        LOG.error("not support type, type:{}", type);
        throw new RuntimeException("not support type");
    }

    public static String joinCollection(String type, Object value) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(value, "value");

        boolean enabled = value instanceof Collection;
        if (enabled == false) {
            throw new RuntimeException("no collection, class:" + value.getClass().getName());
        }
        if (JavaValueTypes.STRING.equals(type)) {
            return JoinUtils.joinSafe((Collection<?>) value);
        }
        if (JavaValueTypes.LONG.equals(type)) {
            return JoinUtils.join((Collection<?>) value);
        }
        if (JavaValueTypes.INTEGER.equals(type)) {
            return JoinUtils.join((Collection<?>) value);
        }
        if (JavaValueTypes.BOOLEAN.equals(type)) {
            return JoinUtils.join((Collection<?>) value);
        }
        if (JavaValueTypes.DOUBLE.equals(type)) {
            return JoinUtils.join((Collection<?>) value);
        }
        LOG.error("not support type, type:{}", type);
        throw new RuntimeException("not support type");
    }
}
