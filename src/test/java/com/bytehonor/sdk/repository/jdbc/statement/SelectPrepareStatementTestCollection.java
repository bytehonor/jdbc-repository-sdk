package com.bytehonor.sdk.repository.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.repository.jdbc.util.SqlPrinter;

public class SelectPrepareStatementTestCollection {

    @Test
    public void test() {
        Set<Integer> ages = new HashSet<Integer>();
        ages.add(1);
        ages.add(2);
        ages.add(3);
        Set<String> names = new HashSet<String>();
        names.add("aa");
        names.add("bb");
        names.add("cc");
        SqlCondition condition = SqlCondition.create();
        condition.in("nickname", names, String.class);
        condition.in("age", ages, Integer.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.desc("age");
        PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE nickname IN ('aa','bb','cc') AND age IN (1,2,3) AND create_at > ? ORDER BY age DESC LIMIT 0,20";
        assertTrue("testSetString", target.equals(sql) && args.length == 1);
    }

}
