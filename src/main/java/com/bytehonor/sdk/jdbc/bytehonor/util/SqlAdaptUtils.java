package com.bytehonor.sdk.jdbc.bytehonor.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.constant.JavaValueTypes;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelColumnValue;
import com.bytehonor.sdk.jdbc.bytehonor.sql.PrepareStatement;
import com.bytehonor.sdk.lang.bytehonor.getter.BooleanGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.DoubleGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.IntegerGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.LongGetter;

public class SqlAdaptUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlAdaptUtils.class);

    public static PreparedStatement convert(PrepareStatement statement, List<ModelColumnValue> items,
            Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(statement.sql(), Statement.RETURN_GENERATED_KEYS);
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
}
