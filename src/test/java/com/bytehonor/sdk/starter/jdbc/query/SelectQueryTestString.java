package com.bytehonor.sdk.starter.jdbc.query;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.starter.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.starter.jdbc.statement.SelectPrepareStatement;
import com.bytehonor.sdk.starter.jdbc.util.SqlPrinter;

public class SelectQueryTestString {

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
        QueryCondition condition = QueryCondition.and();
        condition.in(Student::getAge, ages);
        condition.in(Student::getNickname, names);
        condition.gt(Student::getCreateAt, System.currentTimeMillis());
        condition.desc(Student::getAge);
        PrepareStatement statement = new SelectPrepareStatement(Student.class, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE age IN (1,2,3) AND nickname IN ('aa','bb','cc') AND create_at > ? ORDER BY age DESC LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 1);
    }
}
