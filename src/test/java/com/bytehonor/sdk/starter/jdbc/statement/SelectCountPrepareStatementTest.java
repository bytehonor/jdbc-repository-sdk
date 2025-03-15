package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;

public class SelectCountPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(SelectPrepareStatementTest.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        SqlCondition condition = SqlCondition.create();
        condition.in("age", set, Integer.class);
        condition.gt("createAt", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.desc("age");
        PrepareStatement statement = new SelectCountPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "SELECT COUNT(id) FROM tbl_student WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ?";
        assertTrue("test", target.equals(sql) && args.length == 3);
    }

    @Test
    public void testNonFilter() {
        SqlCondition condition = SqlCondition.create();
        PrepareStatement statement = new SelectCountPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testNonFilter:{}", sql);
        statement.check();

        assertTrue("testNonFilter", "SELECT COUNT(id) FROM tbl_student".equals(sql) && args.length == 0);
    }
}
