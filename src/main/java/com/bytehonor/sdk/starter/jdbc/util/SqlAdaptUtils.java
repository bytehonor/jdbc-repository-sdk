package com.bytehonor.sdk.starter.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.constant.JavaValueTypes;
import com.bytehonor.sdk.lang.spring.getter.BooleanGetter;
import com.bytehonor.sdk.lang.spring.getter.DoubleGetter;
import com.bytehonor.sdk.lang.spring.getter.IntegerGetter;
import com.bytehonor.sdk.lang.spring.getter.LongGetter;
import com.bytehonor.sdk.lang.spring.util.JoinUtils;
import com.bytehonor.sdk.starter.jdbc.constant.SqlValueTypes;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;

public class SqlAdaptUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlAdaptUtils.class);

    private static final Map<Integer, String> SQL_JAVA = new HashMap<Integer, String>();

    private static final Map<String, Integer> JAVA_SQL = new HashMap<String, Integer>();

    static {
        SQL_JAVA.put(SqlValueTypes.STRING, JavaValueTypes.STRING);
        SQL_JAVA.put(SqlValueTypes.LONG, JavaValueTypes.LONG);
        SQL_JAVA.put(SqlValueTypes.INTEGER, JavaValueTypes.INTEGER);
        SQL_JAVA.put(SqlValueTypes.BOOLEAN, JavaValueTypes.BOOLEAN);
        SQL_JAVA.put(SqlValueTypes.DOUBLE, JavaValueTypes.DOUBLE);

        JAVA_SQL.put(JavaValueTypes.STRING, SqlValueTypes.STRING);
        JAVA_SQL.put(JavaValueTypes.LONG, SqlValueTypes.LONG);
        JAVA_SQL.put(JavaValueTypes.INTEGER, SqlValueTypes.INTEGER);
        JAVA_SQL.put(JavaValueTypes.BOOLEAN, SqlValueTypes.BOOLEAN);
        JAVA_SQL.put(JavaValueTypes.DOUBLE, SqlValueTypes.DOUBLE);
    }

    public static PreparedStatement make(String sql, List<ModelKeyValue> items, Connection connection)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        for (ModelKeyValue item : items) {
            set(ps, idx, item);
            idx++;
        }
        return ps;
    }

    private static void set(PreparedStatement ps, int idx, ModelKeyValue item) throws SQLException {
        String javaType = item.getJavaType();
        if (JavaValueTypes.STRING.equals(javaType)) {
            ps.setString(idx, item.getValue().toString());
            return;
        }
        if (JavaValueTypes.LONG.equals(javaType)) {
            ps.setLong(idx, LongGetter.optional(item.getValue().toString(), 0L));
            return;
        }
        if (JavaValueTypes.INTEGER.equals(javaType)) {
            ps.setInt(idx, IntegerGetter.optional(item.getValue().toString(), 0));
            return;
        }
        if (JavaValueTypes.BOOLEAN.equals(javaType)) {
            ps.setBoolean(idx, BooleanGetter.optional(item.getValue().toString(), false));
            return;
        }
        if (JavaValueTypes.DOUBLE.equals(javaType)) {
            ps.setDouble(idx, DoubleGetter.optional(item.getValue().toString(), 0.0));
            return;
        }
        LOG.error("not support, key:{}, value:{}, type:{}", item.getKey(), item.getValue(), javaType);
        throw new JdbcSdkException("not support type");
    }

    public static int toSqlType(String type) {
        Integer val = JAVA_SQL.get(type);
        if (val != null) {
            return val;
        }
        LOG.error("not support type, type:{}", type);
        throw new JdbcSdkException("not support type");
    }

    public static String toJavaType(int type) {
        String val = SQL_JAVA.get(type);
        if (val != null) {
            return val;
        }
        LOG.error("not support type, type:{}", type);
        throw new JdbcSdkException("not support type");
    }

    public static String joinCollection(String type, Object value) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(value, "value");

        boolean enabled = value instanceof Collection;
        if (enabled == false) {
            throw new JdbcSdkException("no collection, class:" + value.getClass().getName());
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
        throw new JdbcSdkException("not support type");
    }
}
