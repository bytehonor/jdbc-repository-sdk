package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;

public class GroupCountPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(GroupCountPrepareStatementTest.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        SqlCondition condition = SqlCondition.create();
        condition.in("age", set, Integer.class);
        condition.gt("createAt", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.desc("age");
        PrepareStatement statement = new GroupCountPrepareStatement(Student.class, Student::getAge, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "SELECT `age` AS `key`, COUNT(id) AS `value` FROM tbl_student WHERE age IN (1,2,3) AND create_at > ? AND nickname LIKE ? GROUP BY `age`";
        assertTrue("test", target.equals(sql) && args.length == 2);
    }

}
