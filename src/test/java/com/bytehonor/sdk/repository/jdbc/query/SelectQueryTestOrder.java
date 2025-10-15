package com.bytehonor.sdk.repository.jdbc.query;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.repository.jdbc.Student;
import com.bytehonor.sdk.repository.jdbc.sql.SqlConvertor;
import com.bytehonor.sdk.repository.jdbc.statement.PrepareStatement;
import com.bytehonor.sdk.repository.jdbc.statement.SelectPrepareStatement;
import com.bytehonor.sdk.repository.jdbc.util.SqlPrinter;

public class SelectQueryTestOrder {

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.and();
        condition.in(Student::getAge, set);
        condition.gt(Student::getCreateAt, System.currentTimeMillis());
        condition.like(Student::getNickname, "boy");
        condition.desc(Student::getCreateAt);
        PrepareStatement statement = new SelectPrepareStatement(Student.class, SqlConvertor.convert(condition));
        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ? ORDER BY create_at DESC LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }
}
