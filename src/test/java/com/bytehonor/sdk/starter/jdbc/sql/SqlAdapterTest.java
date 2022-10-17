package com.bytehonor.sdk.starter.jdbc.sql;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;

public class SqlAdapterTest {

    private static final Logger LOG = LoggerFactory.getLogger(SqlAdapterTest.class);

    @Test
    public void testConvert() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.and();
        condition.ini(Student::getAge, set);
        condition.gt(Student::getCreateAt, System.currentTimeMillis());
        condition.like(Student::getNickname, "boy");
        condition.desc(Student::getAge);

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
