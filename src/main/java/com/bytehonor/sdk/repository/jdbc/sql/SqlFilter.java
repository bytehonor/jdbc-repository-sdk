package com.bytehonor.sdk.repository.jdbc.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.framework.lang.constant.SqlOperator;
import com.bytehonor.sdk.framework.lang.string.StringKit;
import com.bytehonor.sdk.repository.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.repository.jdbc.constant.SqlValueTypes;
import com.bytehonor.sdk.repository.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.repository.jdbc.util.SqlInjectUtils;

/**
 * 过滤条件集合，提供过滤列工厂与值格式化能力。
 *
 * @author lijianqiang
 */
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

    /**
     * 根据操作符对值进行归一化处理（主要用于 LIKE 家族）。
     *
     * @param column 过滤列
     * @return 归一化后的值
     */
    public static Object valueOf(SqlFilterColumn column) {
        Object value = column.getValue();
        Object copy = "";
        SqlOperator operator = column.getOperator();
        if (SqlOperator.LIKE.equals(operator)) {
            copy = SqlInjectUtils.like(value.toString(), true, true);
        } else if (SqlOperator.LIKE_LEFT.equals(operator)) {
            copy = SqlInjectUtils.like(value.toString(), true, false);
        } else if (SqlOperator.LIKE_RIGHT.equals(operator)) {
            copy = SqlInjectUtils.like(value.toString(), false, true);
        } else {
            copy = value;
        }
        return copy;
    }

    public static String patternOf(String key, SqlOperator operator) {
        StringBuilder sb = new StringBuilder();
        sb.append(key).append(SqlConstants.BLANK).append(operator.getOpt()).append(SqlConstants.BLANK);
        sb.append(SqlConstants.PARAM);
        return sb.toString();
    }

    /**
     * IN 条件字符串拼接。
     *
     * @param key 列名
     * @param value 集合值
     * @param javaType Java 类型
     * @return SQL 片段
     */
    public static Object patternIn(String key, Object value, String javaType) {
        StringBuilder sb = new StringBuilder();
        sb.append(key).append(" IN (").append(SqlAdaptUtils.joinCollection(javaType, value)).append(")");
        return sb.toString();
    }

    /**
     * 单个过滤列定义。
     *
     * @author lijianqiang
     */
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
         * 过滤列有效性检查。
         * <p>
         * 允许空字符串，但不允许 null；LIKE 额外要求值非空字符串。
         * </p>
         *
         * @param filter 过滤列
         * @return 是否可接受
         */
        public static boolean accept(SqlFilterColumn filter) {
            if (filter == null) {
                return false;
            }

            if (StringKit.isEmpty(filter.getKey())) {
                return false;
            }

            if (filter.getValue() == null) {
                // 仅校验非 null，空字符串在非 LIKE 场景可作为合法值。
                return false;
            }

            if (SqlOperator.LIKE.equals(filter.getOperator()) && StringKit.isEmpty(filter.getValue().toString())) {
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
            return create(key, value, type.getName(), SqlOperator.IN);
        }

//        public static <T> SqlFilterColumn in(String key, Collection<T> value, String type) {
//            String src = null;
//            if (value != null && value.isEmpty() == false) {
//                if (JavaValueTypes.STRING.equals(type)) {
//                    src = JoinUtils.joinSafe(value);
//                } else {
//                    src = JoinUtils.join(value);
//                }
//            }
//            return create(key, src, SqlValueTypes.STRING, SqlOperator.IN);
//        }

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
