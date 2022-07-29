package com.bytehonor.sdk.starter.jdbc.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class Results {

    public static String string(ResultSet rs, String column) throws SQLException {
        return rs.getString(columnLabel(column));
    }

    public static long longer(ResultSet rs, String column) throws SQLException {
        return rs.getLong(columnLabel(column));
    }

    public static int integer(ResultSet rs, String column) throws SQLException {
        return rs.getInt(columnLabel(column));
    }

    public static boolean bool(ResultSet rs, String column) throws SQLException {
        return rs.getBoolean(columnLabel(column));
    }

    public static double doubler(ResultSet rs, String column) throws SQLException {
        return rs.getDouble(columnLabel(column));
    }

    private static String columnLabel(String column) {
        Objects.requireNonNull(column, "column");

        return SqlColumnUtils.camelToUnderline(column);
    }
}
