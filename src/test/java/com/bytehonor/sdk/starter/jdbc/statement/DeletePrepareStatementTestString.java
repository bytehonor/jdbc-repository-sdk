package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlPrinter;

public class DeletePrepareStatementTestString {

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
        condition.in("age", ages, Integer.class);
        condition.in("nickname", names, String.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.desc("age");
        PrepareStatement statement = new DeletePrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "DELETE FROM tbl_student WHERE age IN (1,2,3) AND nickname IN ('aa','bb','cc') AND create_at > ?";
        assertTrue("test", target.equals(sql) && args.length == 1);
    }

}
