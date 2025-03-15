package com.bytehonor.sdk.starter.jdbc.statement;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.util.JacksonUtils;
import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;

public class SelectPrepareStatementTestCollection {

    private static final Logger LOG = LoggerFactory.getLogger(SelectPrepareStatementTestCollection.class);

    @Test
    public void test() {
        Set<Integer> ages = new HashSet<Integer>();
        ages.add(1);
        ages.add(2);
        ages.add(3);
        Set<String> nicknames = new HashSet<String>();
        nicknames.add("boy1");
        nicknames.add("boy3");
        nicknames.add("boy4");
        nicknames.add("boy5");
        SqlCondition condition = SqlCondition.create();
        condition.in("nickname", nicknames, String.class);
        condition.in("age", ages, Integer.class);
        condition.gt("create_at", System.currentTimeMillis());
        condition.desc("age");
        PrepareStatement statement = new SelectPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:[{}]", sql);
        LOG.info("args:({})", JacksonUtils.toJson(args));
        statement.check();

        String target = "SELECT id, nickname, age, update_at, create_at FROM tbl_student WHERE nickname IN (?) AND age IN (1,2,3) AND create_at > ? ORDER BY age DESC LIMIT 0,20";
        assertTrue("testSetString", target.equals(sql) && args.length == 3);
    }

}
