package com.bytehonor.sdk.repository.jdbc.util;

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

import com.bytehonor.sdk.framework.lang.constant.JavaValueTypes;
import com.bytehonor.sdk.framework.lang.getter.BooleanGetter;
import com.bytehonor.sdk.framework.lang.getter.DoubleGetter;
import com.bytehonor.sdk.framework.lang.getter.IntegerGetter;
import com.bytehonor.sdk.framework.lang.getter.LongGetter;
import com.bytehonor.sdk.framework.lang.util.JoinUtils;
import com.bytehonor.sdk.repository.jdbc.constant.SqlValueTypes;
import com.bytehonor.sdk.repository.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.repository.jdbc.model.ModelField;

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

    public static PreparedStatement make(String sql, List<ModelField> fields, Connection connection)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        for (ModelField field : fields) {
            set(ps, idx, field);
            idx++;
        }
        return ps;
    }

    private static void set(PreparedStatement ps, int idx, ModelField field) throws SQLException {
        String javaType = field.getJavaType();
        if (JavaValueTypes.STRING.equals(javaType)) {
            ps.setString(idx, field.getValue().toString());
            return;
        }
        if (JavaValueTypes.LONG.equals(javaType)) {
            ps.setLong(idx, LongGetter.optional(field.getValue().toString(), 0L));
            return;
        }
        if (JavaValueTypes.INTEGER.equals(javaType)) {
            ps.setInt(idx, IntegerGetter.optional(field.getValue().toString(), 0));
            return;
        }
        if (JavaValueTypes.BOOLEAN.equals(javaType)) {
            ps.setBoolean(idx, BooleanGetter.optional(field.getValue().toString(), false));
            return;
        }
        if (JavaValueTypes.DOUBLE.equals(javaType)) {
            ps.setDouble(idx, DoubleGetter.optional(field.getValue().toString(), 0.0));
            return;
        }
        LOG.error("not support, key:{}, value:{}, type:{}", field.getKey(), field.getValue(), javaType);
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
        throw new JdbcSdkException("not support type:" + type);
    }
}
