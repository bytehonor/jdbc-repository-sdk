package com.bytehonor.sdk.starter.jdbc.sql;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.define.spring.query.QueryCondition;

public class SqlAdapterTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlAdapterTest.class);

    @Test
    public void testConvert() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.and();
        condition.integers("age", set);
        condition.gt("createAt", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.descBy("age");

        SqlCondition model = SqlAdapter.convert(condition);

        String sql = model.toSql().trim();
        String target = "WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ?";
        LOG.info("sql:{}, descBy:{}", sql, model.getOrder().getKey());
        assertTrue("test", target.equals(sql));
    }

    @Test
    public void testFrom() {
    }

    @Test
    public void testInValue() {
    }

}
