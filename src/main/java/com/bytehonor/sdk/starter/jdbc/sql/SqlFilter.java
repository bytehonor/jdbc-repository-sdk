package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.lang.spring.constant.JavaValueTypes;
import com.bytehonor.sdk.lang.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.lang.spring.util.JoinUtils;
import com.bytehonor.sdk.starter.jdbc.constant.SqlValueTypes;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class SqlFilter {

    private final Set<String> keys;

    private final List<SqlFilterColumn> columns;

    private SqlFilter() {
        this.keys = new HashSet<String>();
        this.columns = new ArrayList<SqlFilterColumn>();
    }

    public static SqlFilter plain() {
        return new SqlFilter();
    }

    public List<SqlFilterColumn> getColumns() {
        return columns;
    }

    public SqlFilter with(SqlFilterColumn column) {
        if (SqlFilterColumn.accept(column)) {
            this.keys.add(column.getKey());
            this.columns.add(column);
        }
        return this;
    }

    public boolean contains(String key) {
        return this.keys.contains(key);
    }

    public boolean canFilter() {
        return CollectionUtils.isEmpty(columns) == false;
    }

    public String getString(String key, String operator) {
        if (contains(key) == false) {
            return "";
        }

        String value = "";
        for (SqlFilterColumn column : columns) {
            if (Objects.equals(key, column.getValue()) || Objects.equals(operator, column.getOperator().getKey())) {
                value = column.getValue().toString();
                break;
            }
        }
        return value;
    }

    public static Object valueOf(SqlFilterColumn column) {
        Object value = column.getValue();
        Object copy = "";
        if (SqlOperator.LIKE.equals(column.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), true, true);
        } else if (SqlOperator.LIKE_LEFT.equals(column.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), true, false);
        } else if (SqlOperator.LIKE_RIGHT.equals(column.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), false, true);
        } else {
            copy = value;
        }
        return copy;
    }

    public static final class SqlFilterColumn {

        /**
         * 忽略驼峰或下划线
         */
        private final String key;

        private final Object value;

        private final int sqlType;

        private final String javaType;

        private final SqlOperator operator;

        private SqlFilterColumn(String key, Object value, int sqlType, String javaType, SqlOperator operator) {
            this.key = key;
            this.value = value;
            this.sqlType = sqlType;
            this.javaType = javaType;
            this.operator = operator;
        }

        public static SqlFilterColumn create(String key, Object value, int sqlType, SqlOperator operator) {
            return new SqlFilterColumn(key, value, sqlType, SqlAdaptUtils.toJavaType(sqlType), operator);
        }

        public static SqlFilterColumn create(String key, Object value, String javaType, SqlOperator operator) {
            return new SqlFilterColumn(key, value, SqlAdaptUtils.toSqlType(javaType), javaType, operator);
        }

        /**
         * 允许空字符串做value
         * 
         * @param filter
         * @return
         */
        public static boolean accept(SqlFilterColumn filter) {
            if (filter == null) {
                return false;
            }

            if (SpringString.isEmpty(filter.getKey())) {
                return false;
            }

            if (filter.getValue() == null) {
                // 仅非null判断
                return false;
            }

            if (SqlOperator.LIKE.equals(filter.getOperator()) && SpringString.isEmpty(filter.getValue().toString())) {
                return false;
            }

            SqlColumnUtils.acceptChar(filter.getKey());

            return true;
        }

        public static SqlFilterColumn eq(String key, String value) {
            return create(key, value, SqlValueTypes.STRING, SqlOperator.EQ);
        }

        public static SqlFilterColumn eq(String key, Long value) {
            return create(key, value, SqlValueTypes.LONG, SqlOperator.EQ);
        }

        public static SqlFilterColumn eq(String key, Integer value) {

            return create(key, value, SqlValueTypes.INTEGER, SqlOperator.EQ);
        }

        public static SqlFilterColumn eq(String key, Boolean value) {
            return create(key, value, SqlValueTypes.BOOLEAN, SqlOperator.EQ);
        }

        public static SqlFilterColumn neq(String key, String value) {
            return create(key, value, SqlValueTypes.STRING, SqlOperator.NEQ);
        }

        public static SqlFilterColumn neq(String key, Long value) {
            return create(key, value, SqlValueTypes.LONG, SqlOperator.NEQ);
        }

        public static SqlFilterColumn neq(String key, Integer value) {
            return create(key, value, SqlValueTypes.INTEGER, SqlOperator.NEQ);
        }

        public static SqlFilterColumn neq(String key, Boolean value) {
            return create(key, value, SqlValueTypes.BOOLEAN, SqlOperator.NEQ);
        }

        public static SqlFilterColumn gt(String key, Long value) {
            return create(key, value, SqlValueTypes.LONG, SqlOperator.GT);
        }

        public static SqlFilterColumn gt(String key, Integer value) {
            return create(key, value, SqlValueTypes.INTEGER, SqlOperator.GT);
        }

        public static SqlFilterColumn egt(String key, Long value) {
            return create(key, value, SqlValueTypes.LONG, SqlOperator.EGT);
        }

        public static SqlFilterColumn egt(String key, Integer value) {
            return create(key, value, SqlValueTypes.INTEGER, SqlOperator.EGT);
        }

        public static SqlFilterColumn lt(String key, Long value) {
            return create(key, value, SqlValueTypes.LONG, SqlOperator.LT);
        }

        public static SqlFilterColumn lt(String key, Integer value) {
            return create(key, value, SqlValueTypes.INTEGER, SqlOperator.LT);
        }

        public static SqlFilterColumn elt(String key, Long value) {
            return create(key, value, SqlValueTypes.LONG, SqlOperator.ELT);
        }

        public static SqlFilterColumn elt(String key, Integer value) {
            return create(key, value, SqlValueTypes.INTEGER, SqlOperator.ELT);
        }

        public static SqlFilterColumn like(String key, String value) {
            return create(key, SqlInjectUtils.like(value, true, true), SqlValueTypes.STRING, SqlOperator.LIKE);
        }

        public static SqlFilterColumn likeLeft(String key, String value) {
            return create(key, SqlInjectUtils.like(value, true, false), SqlValueTypes.STRING, SqlOperator.LIKE_LEFT);
        }

        public static SqlFilterColumn likeRight(String key, String value) {
            return create(key, SqlInjectUtils.like(value, false, true), SqlValueTypes.STRING, SqlOperator.LIKE_RIGHT);
        }

        public static <T> SqlFilterColumn in(String key, Collection<T> value, Class<T> type) {
            return in(key, value, type.getName());
        }

        public static <T> SqlFilterColumn in(String key, Collection<T> value, String type) {
            String src = null;
            if (value != null && value.isEmpty() == false) {
                if (JavaValueTypes.STRING.equals(type)) {
                    src = JoinUtils.joinSafe(value);
                } else {
                    src = JoinUtils.join(value);
                }
            }
            return create(key, src, SqlValueTypes.STRING, SqlOperator.IN);
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public int getSqlType() {
            return sqlType;
        }

        public String getJavaType() {
            return javaType;
        }

        public SqlOperator getOperator() {
            return operator;
        }

    }

}
