package com.bytehonor.sdk.repository.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.repository.jdbc.StudentContact;
import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.repository.jdbc.sql.rewrite.PrefixRewriter;

public class LeftJoinPrepareStatementTestNoPager {

    private static final Logger LOG = LoggerFactory.getLogger(LeftJoinPrepareStatementTestNoPager.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        SqlCondition condition = SqlCondition.create(PrefixRewriter.of("m."));
        condition.in("age", set, Integer.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.desc("id");
        condition.limit(-1);
        LeftJoinPrepareStatement statement = new LeftJoinPrepareStatement(StudentContact.class, condition);
        String sql = statement.sql();
        boolean hasError = false;
        try {
            statement.args();
        } catch (Exception e) {
            hasError = true;
        }

        LOG.info("sql:[{}]", sql);

        String target = "SELECT m.id, m.nickname, s.phone, m.update_at, m.create_at FROM tbl_student as m LEFT JOIN tbl_user_profile as s ON m.nickname = s.nickname WHERE m.age IN (1,2,3) AND m.create_at > ? AND m.nickname LIKE ? ORDER BY m.id DESC";
        assertTrue("testNoPager", target.equals(sql) && hasError);
    }
}
