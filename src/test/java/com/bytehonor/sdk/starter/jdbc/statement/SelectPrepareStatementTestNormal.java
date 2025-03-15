package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;

public class SelectPrepareStatementTestNormal {

    private static final Logger LOG = LoggerFactory.getLogger(SelectPrepareStatementTestNormal.class);

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

        LOG.info("test sql:[{}]", sql);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE age IN ? AND create_at > ? AND nickname LIKE ? ORDER BY age DESC LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 3);
    }

}
