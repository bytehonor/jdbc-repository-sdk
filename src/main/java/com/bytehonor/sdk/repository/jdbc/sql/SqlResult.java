package com.bytehonor.sdk.repository.jdbc.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;

public class SqlResult {

    public static String string(ResultSet rs, String column) throws SQLException {
        return rs.getString(underline(column));
    }

    public static long longer(ResultSet rs, String column) throws SQLException {
        return rs.getLong(underline(column));
    }

    public static int integer(ResultSet rs, String column) throws SQLException {
        return rs.getInt(underline(column));
    }

    public static boolean bool(ResultSet rs, String column) throws SQLException {
        return rs.getBoolean(underline(column));
    }

    public static double doubler(ResultSet rs, String column) throws SQLException {
        return rs.getDouble(underline(column));
    }

    private static String underline(String column) {
        Objects.requireNonNull(column, "column");

        return SqlColumnUtils.camelToUnderline(column);
    }
}
