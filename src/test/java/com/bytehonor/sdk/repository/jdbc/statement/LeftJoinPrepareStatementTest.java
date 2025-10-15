package com.bytehonor.sdk.repository.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.bytehonor.sdk.repository.jdbc.StudentContact;
import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.repository.jdbc.sql.rewrite.PrefixRewriter;
import com.bytehonor.sdk.repository.jdbc.util.SqlPrinter;

public class LeftJoinPrepareStatementTest {

    @Test
    public void test() {
        Set<Integer> ages = new HashSet<Integer>();
        ages.add(1);
        ages.add(2);
        ages.add(3);
        SqlCondition condition = SqlCondition.create(PrefixRewriter.of("m."));
        condition.in("age", ages, Integer.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.desc("id");
        LeftJoinPrepareStatement statement = new LeftJoinPrepareStatement(StudentContact.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        SqlPrinter.print(sql, args);
        statement.check();

        String target = "SELECT m.id, m.nickname, s.phone, m.update_at, m.create_at FROM tbl_student as m LEFT JOIN tbl_user_profile as s ON m.nickname = s.nickname WHERE m.age IN (1,2,3) AND m.create_at > ? AND m.nickname LIKE ? ORDER BY m.id DESC LIMIT 0,20";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }

}
