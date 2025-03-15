package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlPrinter;

public class UpdatePrepareStatementTestString {

    @Test
    public void test() {
        Set<String> names = new HashSet<String>();
        names.add("aa");
        names.add("bb");
        names.add("cc");
        SqlCondition condition = SqlCondition.create();
        condition.in("nickname", names, String.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.desc("age");

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new UpdatePrepareStatement(Student.class, condition);
        statement.prepare(student, Student.MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "UPDATE tbl_student SET age = ?,update_at = ? WHERE nickname IN ('aa','bb','cc') AND create_at > ?";
        assertTrue("test", target.equals(sql) && args.length == 3);
    }

}
