package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlPrinter;

public class SelectPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(SelectPrepareStatementTest.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        SqlCondition condition = SqlCondition.create();
        condition.in("age", set, Integer.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.desc("age");
        PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ? ORDER BY age DESC LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }

    @Test
    public void testNonFilter() {
        SqlCondition condition = SqlCondition.create();
        PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student LIMIT 0,20";
        assertTrue("testNonFilter", target.equals(sql) && args.length == 0);
    }

    @Test
    public void testNonFilterNoPage() {
        SqlCondition condition = SqlCondition.create();
        condition.getPager().setLimit(-1);
        PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        boolean hasError = false;
        try {
            statement.args();
        } catch (Exception e) {
            hasError = true;
            LOG.error("error {}", e.getMessage());
        }

        LOG.info("testNonFilterNoPage sql:[{}]", sql);

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student";
        assertTrue("testNonFilterNoPage", target.equals(sql) && hasError == false);
    }

    @Test
    public void testEqEmpty() {
        SqlCondition condition = SqlCondition.create();
        condition.eq("nickname", "");
        PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testEqEmpty sql:[{}]", sql);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE nickname = ? LIMIT 0,20";
        assertTrue("testEqEmpty", target.equals(sql) && args.length == 1);
    }

    @Test
    public void testTime() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        SqlCondition condition = SqlCondition.create();
        condition.in("age", set, Integer.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.desc("age");

        int size = 100000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
            statement.sql();
            statement.args();
        }

        LOG.info("testTime cost:{}", System.currentTimeMillis() - start);
    }

    @Test
    public void testLimitOne() {
        Set<String> names = new HashSet<String>();
        names.add("aa");
        names.add("bb");
        names.add("cc");
        SqlCondition condition = SqlCondition.create();
        condition.in("nickname", names, String.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.desc("age");
        condition.limit(1);
        PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testLimitOne sql:[{}]", sql);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE nickname IN ('aa','bb','cc') AND create_at > ? ORDER BY age DESC LIMIT 0,1";
        assertTrue("testLimitOne", target.equals(sql) && args.length == 1);
    }
}
