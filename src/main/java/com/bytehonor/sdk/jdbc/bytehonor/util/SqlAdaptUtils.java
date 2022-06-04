package com.bytehonor.sdk.jdbc.bytehonor.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.constant.JavaValueTypes;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlValueTypes;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelColumnValue;
import com.bytehonor.sdk.lang.bytehonor.getter.BooleanGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.DoubleGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.IntegerGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.LongGetter;

public class SqlAdaptUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlAdaptUtils.class);

    public static PreparedStatement convert(String sql, List<ModelColumnValue> items, Connection connection)
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
}
