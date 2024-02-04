package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;

public class DeletePrepareStatementTest {

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
        PrepareStatement statement = new DeletePrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "DELETE FROM tbl_student WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ?";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }

    @Test
    public void testNoCondition() {
        SqlCondition condition = SqlCondition.create();
        PrepareStatement statement = new DeletePrepareStatement(Student.class, condition);
        String sql = statement.sql();

        LOG.info("testNoCondition sql:{}", sql);
        boolean hasError = false;
        try {
            statement.args();
        } catch (Exception e) {
            hasError = true;
            LOG.error("error {}", e.getMessage());
        }
        String target = "DELETE FROM tbl_student";
        assertTrue("testNoCondition", target.equals(sql) && hasError);
    }

    @Test
    public void testValueNull() {
        String uuid = null;
        SqlCondition condition = SqlCondition.create().eq("uuid", uuid);
        PrepareStatement statement = new DeletePrepareStatement(Student.class, condition);
        String sql = statement.sql();

        LOG.info("testValueNull sql:{}", sql);
        boolean hasError = false;
        try {
            statement.args();
        } catch (Exception e) {
            hasError = true;
            LOG.error("error {}", e.getMessage());
        }
        String target = "DELETE FROM tbl_student";
        assertTrue("testValueNull", target.equals(sql) && hasError);
    }
    
    @Test
    public void testStringEmpty() {
        String uuid = "";
        SqlCondition condition = SqlCondition.create().eq("uuid", uuid); // 空值会被查询
        PrepareStatement statement = new DeletePrepareStatement(Student.class, condition);
        String sql = statement.sql();

        LOG.info("testStringEmpty sql:{}", sql);
        boolean hasError = true;
        try {
            statement.args();
        } catch (Exception e) {
            hasError = false;
            LOG.error("error {}", e.getMessage());
        }
        String target = "DELETE FROM tbl_student WHERE uuid = ?";
        assertTrue("testStringEmpty", target.equals(sql) && hasError);
    }
}
