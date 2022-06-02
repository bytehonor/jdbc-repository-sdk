package com.bytehonor.sdk.jdbc.bytehonor.sql;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.Student;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;

public class CountPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(SelectPrepareStatementTest.class);

    @Test
    public void test() {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.create();
        condition.inInt("age", set);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.descBy("age");
        PrepareStatement statement = new CountPrepareStatement(Student.class, condition);
        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        for (Object arg : args) {
            LOG.info("arg:{}", arg);
        }

        assertTrue("test", args.length == 2);
    }

}
