package com.bytehonor.sdk.repository.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.repository.jdbc.util.SqlPrinter;

public class SelectPrepareStatementTestOrderDouble {

    @Test
    public void test() {
        Set<Integer> ages = new HashSet<Integer>();
        ages.add(1);
        ages.add(2);
        ages.add(3);
        SqlCondition condition = SqlCondition.create();
        condition.in("age", ages, Integer.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.desc("age");
        condition.asc("create_at");
        PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ? ORDER BY age DESC, create_at ASC LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }

}
